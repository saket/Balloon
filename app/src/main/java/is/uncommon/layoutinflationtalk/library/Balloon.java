package is.uncommon.layoutinflationtalk.library;

import android.support.annotation.AttrRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Start with {@link #builder()}.
 */
public class Balloon {

    private static Config config;

    public static Builder builder() {
        return new Builder();
    }

    public static Config config() {
        if (config == null) {
            config = new Config(Collections.<Integer>emptyList(), Collections.<Integer, BalloonAttributeListener>emptyMap());
        }
        return config;
    }

    public static class Config {

        private List<Integer> registeredAttributeIds;
        private Map<Integer, BalloonAttributeListener> registeredAttrListeners;

        public Config(List<Integer> attributeIds, Map<Integer, BalloonAttributeListener> attributeListeners) {
            registeredAttributeIds = attributeIds;
            registeredAttrListeners = attributeListeners;
        }

        public List<Integer> registeredAttributeIds() {
            return registeredAttributeIds;
        }

        public BalloonAttributeListener attrListenerFor(@AttrRes int attrRes) {
            return registeredAttrListeners.get(attrRes);
        }

    }

    public static class Builder {

        private final List<Integer> attributeIds = new ArrayList<>(8);
        private final Map<Integer, BalloonAttributeListener> attributeListeners = new HashMap<>(8, 1);

        /**
         * Register a listener for a custom attribute.
         *
         * @param attributeResId The attribute to listen for. This must be defined in a resource file (like attrs.xml).
         * @param listener       A listener that will get called everytime <var>attributeResId</var> is found in any View.
         */
        public Builder registerAttr(@AttrRes int attributeResId, BalloonAttributeListener listener) {
            if (listener == null) {
                throw new NullPointerException("Listener cannot be null");
            }

            attributeIds.add(attributeResId);
            attributeListeners.put(attributeResId, listener);
            return this;
        }

        public void build() {
            Balloon.config = new Config(attributeIds, attributeListeners);
        }

    }

}
