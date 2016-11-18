package ng.gdg.devfestsw.githubsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ng.gdg.devfestsw.githubsearch.R;
import ng.gdg.devfestsw.githubsearch.util.FontCache;

public class EditText extends android.widget.EditText {

    public EditText(Context context) {
        super(context);

        setFont(context, null);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFont(context, attrs);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFont(context, attrs);
    }

    private void setFont(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditText);
        if (array == null) {
            return;
        }

        String asset = array.getString(R.styleable.EditText_editTextFont);
        if (asset != null) {
            Typeface typeface = FontCache.getTypeface("fonts/" + asset, context);
            setTypeface(typeface);
        }

        array.recycle();
    }
}

