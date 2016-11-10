package is.uncommon.layoutinflationtalk;

import android.text.InputFilter;
import android.widget.EditText;

import is.uncommon.layoutinflationtalk.library.Attribute;
import is.uncommon.layoutinflationtalk.library.BalloonAttributeListener;

/**
 * Listens for "formatter" attribute on EditText Views.
 */
class EditTextFormatterAttributeListener implements BalloonAttributeListener<EditText, Attribute.StringType> {

    @Override
    public void onApplyAttribute(EditText inflatedView, Attribute.StringType attribute) {
        switch (attribute.value()) {
            case "IndianRupees":
                inflatedView.setFilters(new InputFilter[] { new IndianInputFormatter() });
                break;

            case "CreditCardNumber":
                inflatedView.setFilters(new InputFilter[] { new CardInputFormatter() });
                break;
        }
    }

}
