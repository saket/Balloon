package is.uncommon.layoutinflationtalk;

import is.uncommon.layoutinflationtalk.library.Balloon;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        CalligraphyConfig.initDefault(null);

        Balloon.builder()
                .registerAttr(R.attr.formatter, new EditTextFormatterAttributeListener())
//                .registerAttr(R.attr.horizPaddings, new ViewPaddingAttributeListener())
//                .registerAttr(R.attr.vertPaddings, new ViewPaddingAttributeListener())
                .build();
    }

}
