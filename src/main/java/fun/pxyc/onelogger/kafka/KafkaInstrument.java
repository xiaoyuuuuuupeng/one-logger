package fun.pxyc.onelogger.kafka;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.AbstractCoordinator;
import org.apache.kafka.clients.consumer.internals.ConsumerCoordinator;
import org.apache.kafka.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaInstrument {
    private static final Logger log = LoggerFactory.getLogger(KafkaInstrument.class);
    static Field coordinatorField;
    static Field nodeField;
    private static Field producerFactoryField;
    private static final ConcurrentHashMap<Integer, String> addrsMap = new ConcurrentHashMap();
    private static ClassPool pool;

    static {
        initFields4Listener();
        initFields4Producer();
        initPool();
    }

    public KafkaInstrument() {}

    private static void initPool() {
        pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
    }

    private static void initFields4Listener() {
        try {
            coordinatorField = KafkaConsumer.class.getDeclaredField("coordinator");
            coordinatorField.setAccessible(true);
            nodeField = AbstractCoordinator.class.getDeclaredField("coordinator");
            nodeField.setAccessible(true);
        } catch (Throwable t) {
            log.error("cannot update consumer coordinator field");
            coordinatorField = null;
            nodeField = null;
        }
    }

    public static String getIpPort(Consumer consumer) {
        if (coordinatorField != null && nodeField != null) {
            try {
                KafkaConsumer kc = (KafkaConsumer) consumer;
                ConsumerCoordinator cc = (ConsumerCoordinator) coordinatorField.get(kc);
                Node node = (Node) nodeField.get(cc);
                return node.host() + ":" + node.port();
            } catch (Throwable t) {
                log.error("cannot get consumer coordinator field");
                return "0.0.0.0:0";
            }
        } else {
            return "0.0.0.0:0";
        }
    }

    private static void initFields4Producer() {
        try {
            producerFactoryField = KafkaTemplate.class.getDeclaredField("producerFactory");
            producerFactoryField.setAccessible(true);
        } catch (Throwable t) {
            log.error("cannot update KafkaTemplate producerFactory field");
            producerFactoryField = null;
        }
    }

    private static DefaultKafkaProducerFactory getProducerFactory(KafkaTemplate cf) {
        if (producerFactoryField == null) {
            return null;
        } else {
            try {
                DefaultKafkaProducerFactory s = (DefaultKafkaProducerFactory) producerFactoryField.get(cf);
                return s;
            } catch (Throwable t) {
                log.error("cannot get KafkaTemplate producerFactory field");
                return null;
            }
        }
    }

    private static String getTargetAddrsNoCache(KafkaTemplate template) {
        DefaultKafkaProducerFactory f = getProducerFactory(template);
        if (f == null) {
            return null;
        } else {
            Map<String, Object> map = f.getConfigurationProperties();
            if (map == null || map.isEmpty()) {
                return null;
            } else {
                Object o = map.get("bootstrap.servers");
                if (o == null) {
                    return null;
                }
                return o.toString();
            }
        }
    }

    public static String getTargetAddrs(KafkaTemplate template) {
        int hashCode = template.hashCode();
        String addrs = addrsMap.get(hashCode);
        if (addrs != null) {
            return addrs;
        } else {
            addrs = getTargetAddrsNoCache(template);
            if (addrs == null) {
                addrs = "unknown";
            }
            addrsMap.put(hashCode, addrs);
            return addrs;
        }
    }
}
