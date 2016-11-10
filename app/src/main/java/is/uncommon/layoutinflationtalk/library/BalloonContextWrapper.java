package is.uncommon.layoutinflationtalk.library;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * Decorates the framework Context object by providing its own version of LayoutInflater: {@link BalloonLayoutInflater}.
 */
public class BalloonContextWrapper extends ContextWrapper {

    private BalloonLayoutInflater inflater;

    public static BalloonContextWrapper wrap(Context baseContext) {
        return new BalloonContextWrapper(baseContext);
    }

    BalloonContextWrapper(Context base) {
        super(base);
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = new BalloonLayoutInflater(LayoutInflater.from(getBaseContext()), this);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }

}
