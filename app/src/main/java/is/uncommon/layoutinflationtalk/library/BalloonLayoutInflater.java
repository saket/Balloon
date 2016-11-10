package is.uncommon.layoutinflationtalk.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

/**
 * TODO: Explain how this works.
 */
class BalloonLayoutInflater extends LayoutInflater {

    private static final String[] frameworkViewsPackagePrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
    };

    private final BalloonCustomAttributesHandler customPropertiesHandler;
    private Field mCachedViewConstructorArgs = null;

    BalloonLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);

        if (getFactory2() != null && !(getFactory2() instanceof WrapperFactory2)) {
            setFactory2(getFactory2());
        }

        customPropertiesHandler = new BalloonCustomAttributesHandler();
    }

    private void dispatchOnViewCreatedCallback(@NonNull View createdView, AttributeSet attrs) {
        customPropertiesHandler.onViewCreated(createdView, attrs);
    }

    private static class WrapperFactory2 implements Factory2 {
        private final Factory2 wrappedFactory;
        private final BalloonLayoutInflater balloonInflater;

        WrapperFactory2(Factory2 wrappedFactory, BalloonLayoutInflater balloonLayoutInflater) {
            this.wrappedFactory = wrappedFactory;
            this.balloonInflater = balloonLayoutInflater;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            // ViewGroups should get inflated here. AppCompat may also return tint-aware widgets from here, overriding the framework.
            View createdView = wrappedFactory.onCreateView(parent, name, context, attrs);

            if (createdView == null) {
                // Framework Views and custom Views should get inflated here.
                createdView = balloonInflater.inflateViewUsingReflection(parent, name, context, attrs);
            }

            if (createdView != null) {
                balloonInflater.dispatchOnViewCreatedCallback(createdView, attrs);
            }

            return createdView;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            // Called only on pre-Honeycomb devices, which we don't want to support. They can die in hell.
            return null;
        }

    }

    /**
     * Called when none of the Factory associated with this LayoutInflater was able to inflate a View. If we reach here,
     * it must be a framework widget (TextView, RadioButton, etc.).
     */
    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        // This mimics the {@code PhoneLayoutInflater} in the way it tries to inflate the base
        // classes, if this fails its pretty certain the app will fail at this point.
        View createdView = null;
        for (String prefix : frameworkViewsPackagePrefixList) {
            try {
                createdView = createView(name, prefix, attrs);
            } catch (ClassNotFoundException ignored) {
            }
        }

        // In this case we want to let the base class take a crack at it.
        if (createdView == null) {
            createdView = super.onCreateView(name, attrs);
        }

        if (createdView != null) {
            dispatchOnViewCreatedCallback(createdView, attrs);
        }

        return createdView;
    }

    private View inflateViewUsingReflection(View parent, String name, Context context, AttributeSet attrs) {
        if (-1 == name.indexOf('.')) {
            // Framework widget.
            try {
                View createdView = super.onCreateView(parent, name, attrs);
                if (createdView != null) {
                    dispatchOnViewCreatedCallback(createdView, attrs);
                }
                return createdView;

            } catch (ClassNotFoundException ignored) {
                return null;
            }

        } else {
            // Widget name found with its fully qualified name in Xml. Must be a custom View.
            if (mCachedViewConstructorArgs == null) {
                mCachedViewConstructorArgs = ReflectionUtils.getField(LayoutInflater.class, "mConstructorArgs");
            }

            final Object[] constructorArgsArr = (Object[]) ReflectionUtils.getValue(mCachedViewConstructorArgs, this);
            //noinspection ConstantConditions
            final Object lastContext = constructorArgsArr[0];

            // The LayoutInflater actually finds out the correct context to use. We just need to set
            // it on the mConstructor for the internal method. Set the constructor ars up for the
            // createView, not sure why we can't pass these in.
            constructorArgsArr[0] = context;
            ReflectionUtils.setValue(mCachedViewConstructorArgs, this, constructorArgsArr);

            View createdView = null;
            try {
                createdView = createView(name, null, attrs);
            } catch (ClassNotFoundException ignored) {
            } finally {
                // TODO: 07/08/16 Find out why are we updating the context?
                // Probably for no retaining reference on context.
                constructorArgsArr[0] = lastContext;
                ReflectionUtils.setValue(mCachedViewConstructorArgs, this, constructorArgsArr);
            }

            if (createdView != null) {
                dispatchOnViewCreatedCallback(createdView, attrs);
            }

            return createdView;
        }
    }

    /**
     * Create a copy of the existing LayoutInflater object, with the copy pointing to a different Context than the original.
     * This is used by {@link ContextThemeWrapper} to create a new LayoutInflater to go along with the new Context theme.
     *
     * @param newContext The new Context to associate with the new LayoutInflater. May be the same as the original Context
     *                   if desired.
     * @return Returns a brand spanking new LayoutInflater object associated with the given Context.
     */
    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new BalloonLayoutInflater(this, newContext);
    }

    @Override
    public void setFactory2(Factory2 factory) {
        // Only set our wrappedFactory and wrap calls to the Factory trying to be set!
        if (!(factory instanceof WrapperFactory2)) {
            super.setFactory2(new WrapperFactory2(factory, this));
        } else {
            super.setFactory2(factory);
        }
    }

}
