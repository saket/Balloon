package is.uncommon.layoutinflationtalk.library;

import android.view.View;

/**
 * Interface for classes that are registered for custom attributes using {@link Balloon.Builder#registerAttr(int, BalloonAttributeListener)}.
 * All registered instances of this interface will be stored in a static instance, so make sure you're not leaking any memory.
 *
 * @param <V> Type of the View where you're using the attribute. You cab either pass in a specific View like "TextView" or a
 *            generic "View"/"ViewGroup", etc., when the attribute is being used in multiple types of Views.
 */
public interface BalloonAttributeListener<V extends View, A extends Attribute> {

    /**
     * Gets called whenever the registered attribute is found in a View being inflated.
     *
     * @param inflatedView The inflated View.
     * @param attribute    Contains the attribute's name and value.
     */
    void onApplyAttribute(V inflatedView, A attribute);

}
