/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exam.longtian.decode;

import java.util.Collection;
import java.util.HashSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.exam.longtian.R;
import com.google.zxing.ResultPoint;

public final class ViewfinderViewGoods extends View implements View.OnTouchListener{

	private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderViewGoods(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		frameColor = resources.getColor(R.color.viewfinder_frame);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManagerGoods.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		//     Draw the exterior (i.e. outside the framing rect) darkened
		
		/**
		 * 鐢诲嚭鎵弿鍖哄煙
		 * 鍒嗕负鍥涢儴鍒嗭紝椤堕儴銆佸乏渚с�佸彸渚с�佸簳閮�
		 * 璺濈灞忓箷椤堕儴璺濈top
		 * 璺濈灞忓箷宸︿晶100
		 * 璺濈灞忓箷鍙充晶100
		 * 鎵弿鍖哄煙楂樺害150
		 * 杩欎簺鍙傛暟鍦–ameraUtil绫婚噷鍙慨鏀�
		 * 
		 * */
		paint.setColor(R.color.red);
		canvas.drawRect(0, 0, width, CameraUtil.marginTop, paint);//涓�
		canvas.drawRect(0, CameraUtil.marginTop, CameraUtil.marginLeft, CameraUtil.marginTop + CameraUtil.cameraHeight, paint);//宸�
		canvas.drawRect(width - CameraUtil.marginRight, CameraUtil.marginTop, width, CameraUtil.marginTop + CameraUtil.cameraHeight, paint);//鍙�
		canvas.drawRect(0, CameraUtil.marginTop + CameraUtil.cameraHeight, width, CameraUtil.marginTop * 5 + CameraUtil.cameraHeight, paint);//涓嬪崐閮ㄥ垎闃村奖

		// Draw a two pixel solid black border inside the framing rect
//		paint.setColor(frameColor);
//		canvas.drawRect(0, 0, 100, width, paint);
//		canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
//		canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
//		canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

		// Draw a red "laser scanner" line through the middle to show decoding is active
		/**
		 * 鐢讳腑闂寸殑鎵弿瀹氫綅绾�
		 * 绾跨殑楂樺害涓�2
		 * 浣嶄簬鐭╁舰妗嗙殑姝ｄ腑闂�
		 * */
		paint.setColor(laserColor);
		paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
		scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
		
		canvas.drawRect(CameraUtil.marginLeft + 10, CameraUtil.marginTop + CameraUtil.cameraHeight / 2, width - CameraUtil.marginRight - 10, CameraUtil.marginTop + CameraUtil.cameraHeight / 2 + 2, paint);

		Collection<ResultPoint> currentPossible = possibleResultPoints;
		Collection<ResultPoint> currentLast = lastPossibleResultPoints;
		if (currentPossible.isEmpty()) {
			lastPossibleResultPoints = null;
		} else {
			possibleResultPoints = new HashSet<ResultPoint>(5);
			lastPossibleResultPoints = currentPossible;
			paint.setAlpha(OPAQUE);
			paint.setColor(resultPointColor);
			for (ResultPoint point : currentPossible) {
				canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
			}
		}
		if (currentLast != null) {
			paint.setAlpha(OPAQUE / 2);
			paint.setColor(resultPointColor);
			for (ResultPoint point : currentLast) {
				canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
			}
		}

		// Request another update at the animation interval, but only repaint the laser line,
		// not the entire viewfinder mask.
		postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
	}

	public void drawViewfinder() {
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live scanning display.
	 *
	 * @param barcode An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
