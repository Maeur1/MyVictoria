package com.myvictoria.app;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mayur on 4/05/2014.
 */
public class MapFragment extends Fragment implements View.OnTouchListener{

    ImageView im;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        im = (ImageView) rootView.findViewById(R.id.map);
        im.setImageResource(R.drawable.kelburn_map);
        im.setOnTouchListener(this);
        return rootView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int maxX = (int)((1352 / 2) - (screenWidth / 2));
        int maxY = (int)((1352 / 2) - (screenHeight / 2));
        // set scroll limits
        final int maxLeft = (maxX * -1);
        final int maxRight = maxX;
        final int maxTop = (maxY * -1);
        final int maxBottom = maxY;
        float downX = 0, downY = 0;
        int totalX = 0, totalY = 0;
        int scrollByX, scrollByY;
        float currentX, currentY;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                scrollByX = (int)(downX - currentX);
                scrollByY = (int)(downY - currentY);

                // scrolling to left side of image (pic moving to the right)
                if (currentX > downX)
                {
                    if (totalX == maxLeft)
                    {
                        scrollByX = 0;
                    }
                    if (totalX > maxLeft)
                    {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX < maxLeft)
                    {
                        scrollByX = maxLeft - (totalX - scrollByX);
                        totalX = maxLeft;
                    }
                }

                // scrolling to right side of image (pic moving to the left)
                if (currentX < downX)
                {
                    if (totalX == maxRight)
                    {
                        scrollByX = 0;
                    }
                    if (totalX < maxRight)
                    {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX > maxRight)
                    {
                        scrollByX = maxRight - (totalX - scrollByX);
                        totalX = maxRight;
                    }
                }

                // scrolling to top of image (pic moving to the bottom)
                if (currentY > downY)
                {
                    if (totalY == maxTop)
                    {
                        scrollByY = 0;
                    }
                    if (totalY > maxTop)
                    {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY < maxTop)
                    {
                        scrollByY = maxTop - (totalY - scrollByY);
                        totalY = maxTop;
                    }
                }

                // scrolling to bottom of image (pic moving to the top)
                if (currentY < downY)
                {
                    if (totalY == maxBottom)
                    {
                        scrollByY = 0;
                    }
                    if (totalY < maxBottom)
                    {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY > maxBottom)
                    {
                        scrollByY = maxBottom - (totalY - scrollByY);
                        totalY = maxBottom;
                    }
                }

                im.scrollBy(scrollByX, scrollByY);
                downX = currentX;
                downY = currentY;
                break;

        }

        return true;
    }
}
