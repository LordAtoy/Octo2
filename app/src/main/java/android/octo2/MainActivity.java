package android.octo2;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.myimage1).setOnLongClickListener(new MyClickListener());
        findViewById(R.id.myimage2).setOnLongClickListener(new MyClickListener());
        findViewById(R.id.myimage3).setOnLongClickListener(new MyClickListener());
        findViewById(R.id.myimage4).setOnLongClickListener(new MyClickListener());
        findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
        findViewById(R.id.topright).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomleft).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());
    }

    // This defines your touch listener
    private final class MyClickListener implements View.OnLongClickListener {
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            } else {
                view.startDrag(data, shadowBuilder, view, 0);
            }
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_droptarget, null);
        Drawable normalShape = ResourcesCompat.getDrawable(getResources(), R.drawable.shapes, null);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        v.setBackground(enterShape);
                    } else {
                        v.setBackgroundDrawable(enterShape);
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        v.setBackground(normalShape);
                    } else {
                        v.setBackgroundDrawable(normalShape);
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        v.setBackground(normalShape);
                    } else {
                        v.setBackgroundDrawable(normalShape);
                    }
                default:
                    break;
            }
            return true;
        }
    }
}
