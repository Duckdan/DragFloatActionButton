package study.yang.dragfloatbuttonaction;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;



public class DragFloatActionButton extends android.support.v7.widget.AppCompatImageView {

    private int parentHeight;
    private int parentWidth;


    private int lastX;
    private int lastY;


    private ViewGroup parent;
    private OnClickListener onClickListener;

    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int downX = 0;
    private int downY = 0;
    private int upX = 0;
    private int upY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                ViewParent viewParent = getParent();

                downX = lastX = rawX;
                downY = lastY = rawY;
                if (viewParent != null) {
                    //请求父控件不中断事件
                    viewParent.requestDisallowInterceptTouchEvent(true);
                    this.parent = (ViewGroup) viewParent;
                    //获取父控件的高度
                    parentHeight = this.parent.getHeight();
                    //获取父控件的宽度
                    parentWidth = this.parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                //控件距离底部的margin
                double bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,getResources().getDisplayMetrics());
                y = y < 0 ? 0 : (float) (y > parentHeight - getHeight() - bottomMargin ? parentHeight - getHeight() - bottomMargin : y);
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getRawX();
                upY = (int) event.getRawY();
                int distanceX = Math.abs(Math.abs(upX) - Math.abs(downX));
                int distanceY = Math.abs(Math.abs(upY) - Math.abs(downY));
                //当手指按下的事件跟手指抬起事件之间的距离小于10时执行点击事件
                if (Math.max(distanceX, distanceY) <= 10) {
                    if (onClickListener != null) {
                        onClickListener.onClick();
                    }
                }
                moveHide(rawX);
                break;
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return true;
    }


    private void moveHide(int rawX) {
        if (rawX >= parentWidth / 2) {

            //靠右吸附
            animate().setInterpolator(new DecelerateInterpolator())
                    .setDuration(500)
                    .xBy(parentWidth - getWidth() - getX())
                    .start();
        } else {
            //靠左吸附
            ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
            oa.setInterpolator(new DecelerateInterpolator());
            oa.setDuration(500);
            oa.start();

        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick();
    }

}