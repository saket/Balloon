package is.uncommon.layoutinflationtalk.library;

import android.support.annotation.AttrRes;

/**
 *
 */
public class Attribute<T> {

    @AttrRes private int attributeResId;
    private T attributeValue;

    public static class StringType extends Attribute<String> {
        @Override
        public String value() {
            return super.value();
        }
    }

    public static class DimensionType extends Attribute<Integer> {

        /**
         * Get attribute value in pixels.
         */
        @Override
        public Integer value() {
            return super.value();
        }

    }

    public T value() {
        return attributeValue;
    }

    @AttrRes
    public int property() {
        return attributeResId;
    }

}
