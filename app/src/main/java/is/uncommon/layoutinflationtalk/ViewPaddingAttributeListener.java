package is.uncommon.layoutinflationtalk;

import android.view.View;

import is.uncommon.layoutinflationtalk.library.Attribute;
import is.uncommon.layoutinflationtalk.library.BalloonAttributeListener;

public class ViewPaddingAttributeListener implements BalloonAttributeListener<View, Attribute.DimensionType> {

    @Override
    public void onApplyAttribute(View inflatedView, Attribute.DimensionType attribute) {
        switch (attribute.property()) {
            case R.attr.horizPaddings:
                Integer horizPaddings = attribute.value();
                inflatedView.setPadding(horizPaddings, inflatedView.getPaddingTop(), horizPaddings, inflatedView.getPaddingBottom());
                break;

            case R.attr.vertPaddings:
                Integer vertPaddings = attribute.value();
                inflatedView.setPadding(inflatedView.getPaddingLeft(), vertPaddings, inflatedView.getPaddingRight(), vertPaddings);
                break;
        }
    }

}
