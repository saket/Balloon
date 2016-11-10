package is.uncommon.layoutinflationtalk.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

import timber.log.Timber;

/**
 * TODO: Make generic for public usage.
 * TODO: Add doc.
 */
public class BalloonCustomAttributesHandler {

    /**
     * Handle the created view.
     */
    public void onViewCreated(@NonNull View view, AttributeSet attributeSet) {
        if (!(view instanceof AppCompatEditText)) {
            // TODO: 10/11/16 remove this!
            return;
        }

        Timber.i("Created View: %s", view.getClass().getSimpleName());

        try {
            List<Integer> registeredAttributeIds = Balloon.config().registeredAttributeIds();

            for (Integer attributeResId : registeredAttributeIds) {
                Object resolvedValue = resolveAttribute(view.getContext(), attributeSet, attributeResId);

                BalloonAttributeListener attributeListener = Balloon.config().attrListenerFor(attributeResId);
                Timber.i("resolved: %s", resolvedValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Timber.d("--------------------------------------------");
    }

    /**
     * Resolving font path from xml attrs, style attrs or text appearance
     */
    private Object resolveAttribute(Context context, AttributeSet attributeSet, int attributeResId) {
        TypedValue resolvedAttrValue;

        //Timber.i("View…");
        resolvedAttrValue = pullAttributeValueFromView(context, attributeSet, attributeResId);

//        if (resolvedAttrValue == null) {
//            Timber.i("Style…");
//            resolvedAttrValue = pullAttributeValueFromStyle(context, attributeSet, attributeResId);
//        }
//
//        if (resolvedAttrValue == null) {
//            Timber.i("Theme…");
//            resolvedAttrValue = pullAttributeValueFromTheme(context, attributeResId);
//        }

        return resolvedAttrValue != null ? parseTypedValueData(resolvedAttrValue) : null;
    }

    /**
     * Tries to pull the custom attribute directly from the View.
     *
     * @return null if the attribute wasn't found defined on the View.
     */
    @Nullable
    static TypedValue pullAttributeValueFromView(Context context, AttributeSet attributeSet, int attributeResId) {
        /*if (attributeResId == null || attributeSet == null) {
            return null;
        }*/

        final String attributeName;
        try {
            attributeName = context.getResources().getResourceEntryName(attributeResId);
        } catch (Resources.NotFoundException e) {
            // Invalid attribute ID
            return null;
        }
        Timber.i("Attr name: %s", attributeName);
        TypedValue typedValue = new TypedValue();

        boolean resolveAttribute = context.getTheme().resolveAttribute(attributeResId, typedValue, true);
        Timber.i("resolveAttribute: %s", resolveAttribute);

        return typedValue;

        //return attributeSet.getAttributeValue(null, attributeName);
        /*final int resourceId = attributeSet.getAttributeResourceValue(null, attributeName, -1);
        return resourceId > 0
                ? context.getString(resourceId)
                : attributeSet.getAttributeValue(null, attributeName);*/
    }

    /**
     * Tries to pull the custom attribute from the View Style.
     *
     * @return null if the attribute wasn't found defined in the View's style.
     */
    @Nullable
    static TypedValue pullAttributeValueFromStyle(Context context, AttributeSet attributeSet, int attributeResId) {
        // Get the theme defined on the View.
        @StyleRes int style = attributeSet.getStyleAttribute();

        // Skip if the View doesn't have any style.
        if (style == 0) {
            Timber.w("No style found!");
            return null;
        }

        final TypedArray typedArray = context.obtainStyledAttributes(style, new int[] { attributeResId });
        if (typedArray != null) {
            try {
                TypedValue typedValue = new TypedValue();
                boolean valueRetrieved = typedArray.getValue(0, typedValue);

                if (valueRetrieved) {
                    Timber.i("typedValue: %s", typedValue);
                    return typedValue;

                } else {
                    Timber.w("Couldn't read value from styles");
                }
                //return typedArray.getString(0);

            } catch (Exception ignore) {
                // Failed for some reason.
                ignore.printStackTrace();

            } finally {
                typedArray.recycle();
            }
        }
        return null;
    }

    /**
     * Last but not least, try to pull the Font Path from the Theme, if defined.
     */
    @Nullable
    static TypedValue pullAttributeValueFromTheme(Context context, int attributeResId) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeResId, value, true);

        return value;
    }

    private static Object parseTypedValueData(TypedValue value) {
        switch (value.type) {
            case TypedValue.TYPE_STRING:
                return value.string;

            case TypedValue.TYPE_REFERENCE:
                return "@" + value.data;

            case TypedValue.TYPE_ATTRIBUTE:
                return "?" + value.data;

            case TypedValue.TYPE_FLOAT:
                return Float.toString(Float.intBitsToFloat(value.data));

            case TypedValue.TYPE_DIMENSION:
                return TypedValue.complexToFloat(value.data);

            case TypedValue.TYPE_FRACTION:
                return TypedValue.complexToFloat(value.data) * 100;

            case TypedValue.TYPE_INT_HEX:
                return "0x" + Integer.toHexString(value.data);

            case TypedValue.TYPE_INT_BOOLEAN:
                return value.data != 0 ? "true" : "false";

            case TypedValue.TYPE_NULL:
                return null;

            default:
                if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                    return "#" + Integer.toHexString(value.data);

                } else if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT) {
                    return Integer.toString(value.data);
                }
                return null;
        }
    }

}
