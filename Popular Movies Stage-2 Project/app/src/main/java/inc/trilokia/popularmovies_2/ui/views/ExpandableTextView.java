package inc.trilokia.popularmovies_2.ui.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import inc.trilokia.popularmovies_2.R;

public class ExpandableTextView extends android.support.v7.widget.AppCompatTextView implements
        View.OnClickListener {

    //Class Variables
    private int mMaxLines = 5;

    public ExpandableTextView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        post(new Runnable() {
            @Override
            public void run() {
                if (getLineCount() > 5) {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_show_more);
                    setPadding(0, 0, 0, 0);
                }
            }
        });
        setMaxLines(mMaxLines);
    }

    //TODO: Feels clunky, have layout scroll with expansion
    @Override
    public void onClick(View v) {
        if (getLineCount() > 5) {
            if (getMaxLines() > 5) {
                mMaxLines = 5;
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_show_more);
            } else {
                mMaxLines = 300;
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_show_less);
            }

            //TODO: Duration should be relative to length
            ObjectAnimator animator = ObjectAnimator.ofInt(this, "maxLines", mMaxLines);
            animator.setDuration(200).start();
        }
    }

}