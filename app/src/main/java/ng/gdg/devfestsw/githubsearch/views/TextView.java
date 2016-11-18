package ng.gdg.devfestsw.githubsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ng.gdg.devfestsw.githubsearch.R;
import ng.gdg.devfestsw.githubsearch.util.FontCache;

public class TextView extends android.widget.TextView {

    public TextView(Context context) {
        super(context);

        setFont(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFont(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFont(context, attrs);
    }

    private void setFont(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextView);
        if (array == null) {
            return;
        }

        String asset = array.getString(R.styleable.TextView_textViewFont);
        if (asset != null) {
            Typeface typeface = FontCache.getTypeface("fonts/" + asset, context);
            setTypeface(typeface);
        }

        array.recycle();
    }
}
