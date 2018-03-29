package com.exam.longtian.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.view.InputDevice;
import android.view.KeyEvent;
import java.util.Iterator;
import java.util.Set;

/** 
 * ɨ��ǹ�¼������� 
 * 
 * @author yxx
 *
 * @date 2018-3-29 ����1:29:38
 * 
 */
public class ScanGunKeyEventHelper {
	
    private final static long MESSAGE_DELAY = 500;//�ӳ�500ms���ж�ɨ���Ƿ���ɡ�
    private StringBuffer mStringBufferResult;//ɨ������
    private boolean mCaps;//��Сд����
    private final Handler mHandler;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Runnable mScanningFishedRunnable;
    private OnScanSuccessListener mOnScanSuccessListener;
    private String mDeviceName;

    public ScanGunKeyEventHelper(OnScanSuccessListener onScanSuccessListener) {
        mOnScanSuccessListener = onScanSuccessListener ;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mStringBufferResult = new StringBuffer();
        mHandler = new Handler();
        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }
    /**
     * ����ɨ��ɹ���Ľ��
     */
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null)
            mOnScanSuccessListener.onScanSuccess(barcode);
        mStringBufferResult.setLength(0);
    }
    /**
     * ɨ��ǹ�¼�����
     * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //��ĸ��Сд�ж�
        checkLetterStatus(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            char aChar = getInputCode(event);;

            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //��Ϊ�س�����ֱ�ӷ���
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else {
                //�ӳ�post����500ms�ڣ��������¼�
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }

        }
    }

    //���shift��
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //����shift������ʾ��д
                mCaps = true;
            } else {
                //�ɿ�shift������ʾСд
                mCaps = false;
            }
        }
    }
    //��ȡɨ������
    private char getInputCode(KeyEvent event) {
        int keyCode = event.getKeyCode();
        char aChar;
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //��ĸ
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //����
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //��������
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }
        return aChar;
    }

    public interface OnScanSuccessListener {
        void onScanSuccess(String barcode);
    }

    public void onDestroy() {
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mOnScanSuccessListener = null;
    }
    //�����ֻ������ǣ��޷�ʹ�ø÷���
//    private void hasScanGun() {
//        Configuration cfg = getResources().getConfiguration();
//        return cfg.keyboard != Configuration.KEYBOARD_NOKEYS;
//    }
    /**
     * ɨ��ǹ�Ƿ�����
     * @return
     */
    public boolean hasScanGun() {
        if (mBluetoothAdapter == null) {
            return false;
        }
        Set<BluetoothDevice> blueDevices = mBluetoothAdapter.getBondedDevices();
        if (blueDevices == null || blueDevices.size() <= 0) {
            return false;
        }
        for (Iterator<BluetoothDevice> iterator = blueDevices.iterator(); iterator.hasNext(); ) {
            BluetoothDevice bluetoothDevice = iterator.next();

            if (bluetoothDevice.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.Major.PERIPHERAL) {
                mDeviceName = bluetoothDevice.getName();
                return isInputDeviceExist(mDeviceName);
            }
        }
        return false;
    }
    /**
     * �����豸�Ƿ����
     * @param deviceName
     * @return
     */
    private boolean isInputDeviceExist(String deviceName) {
        int[] deviceIds = InputDevice.getDeviceIds();

        for (int id : deviceIds) {
            if (InputDevice.getDevice(id).getName().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * �Ƿ�Ϊɨ��ǹ�¼�(���ֻ���KeyEvent��ȡ�����ִ���)
     * @param event
     * @return
     */
    @Deprecated
    public boolean isScanGunEvent(KeyEvent event) {
        return event.getDevice().getName().equals(mDeviceName);
    }
}