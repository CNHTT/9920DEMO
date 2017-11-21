package com.szfp.pos.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;
import com.szfp.pos.App;
import com.szfp.pos.Logger;
import com.szfp.pos.R;
import com.szfp.pos.model.Item;
import com.szfp.pos.model.LoginRecord;
import com.szfp.pos.model.PosRecord;
import com.szfp.pos.utils.JsonUtil;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 戴尔 on 2017/11/18.
 */

public class PrintManager {

    private static PrintManager mInstance;

    public PrintManager() {
    }
    private static Context mContext;
    public static PrintManager getmInstance(Context c){
        mContext = c ;
        if(null == mInstance){
            mInstance = new PrintManager();
        }
        return mInstance ;
    }

    private Printer printer = null ;
    private PrintTask printTask = null ;
    private boolean printFlag = false ;

    /**
     * 在画布上画出一条线
     * @param paint
     * @param canvas
     */
    private void printLine(Paint paint , PrintCanvas canvas){
        String line = "----------------------------------------------------------------";
        setFontStyle(paint , 1 , true);
        canvas.drawText(line , paint);
    }
    /**
     * 设置打印字体样式
     * @param paint 画笔
     * @param size 字体大小 1---small , 2---middle , 3---large
     * @param isBold 是否加粗
     * @author zq
     */
    private void setFontStyle(Paint paint , int size , boolean isBold){
        if(isBold)
            paint.setTypeface(Typeface.DEFAULT_BOLD);
        else
            paint.setTypeface(Typeface.DEFAULT) ;
        switch (size) {
            case 0 : break;
            case 1 : paint.setTextSize(16F) ;break;
            case 2 : paint.setTextSize(22F) ;break;
            case 3 : paint.setTextSize(30F) ;break;
            default:break;
        }
    }

    public static String getStrAmount(long Amount) {
        double f1 = Double.valueOf(Amount + "");
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f1 / 100);
    }

    /**
     * 打印
     * @param pCanvas
     * @return
     */
    private int printData(PrintCanvas pCanvas) {
        printTask.setPrintCanvas(pCanvas);
        int ret = printer.getStatus();
        Logger.debug("printer.getStatus="+ret);
        if (ret != Printer.PRINTER_OK)
            return ret;
        printFlag = true;
        printer.startPrint(printTask, new PrinterCallback() {
            @Override
            public void onResult(int i, PrintTask printTask) {
                Logger.debug("PrinterCallback i = " + i);
                printFlag = false;
            }
        });
        return ret;
    }
    /**
     * 检查打印机状态
     * @return
     */
    private int checkPrinterStatus() {
        long t0 = System.currentTimeMillis();
        int ret ;
        while (true) {
            if (System.currentTimeMillis() - t0 > 30000) {
                ret = -1 ;
                break;
            }
            ret = printer.getStatus();
            Logger.debug("printer.getStatus() ret = "+ret);
            if (ret == Printer.PRINTER_OK) {
                Logger.debug("printer.getStatus()=Printer.PRINTER_OK");
                Logger.debug("打印机状态正常");
                break;
            }else if (ret == -3) {
                Logger.debug("printer.getStatus()=Printer.PRINTER_STATUS_PAPER_LACK");
                Logger.debug("提示用户装纸...");
                break;
            } else if (ret == Printer.PRINTER_STATUS_BUSY) {
                Logger.debug("printer.getStatus()=Printer.PRINTER_STATUS_BUSY");
                Logger.debug("打印机忙");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
        return ret;
    }

    public int printLoginRecord(LoginRecord loginRecord) {
        this.printTask = new PrintTask();
        this.printFlag = true;
        int ret ;
        printer = Printer.getInstance() ;
        if(printer == null){
            ToastUtils.showToast("110");
            return 110 ;
        }
        PrintCanvas canvas = new PrintCanvas() ;
        Paint paint = new Paint() ;

        setFontStyle(paint , 2 , true);
        canvas.drawText(App.companyName , paint);
        printLine(paint , canvas);
        setFontStyle(paint , 1 , true);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_costomer_logo);

        canvas.drawBitmap(scaleBitmap(bitmap, 0.3F) , paint);
        if(!bitmap.isRecycled()){
            bitmap.recycle();
        }


        printLine(paint , canvas);
        setFontStyle(paint , 2 , false);
        canvas.drawText(App.slogan, paint);
        canvas.drawText(PrintFont.logoutSummary, paint);
        printLine(paint , canvas);
        setFontStyle(paint , 2 , false);
        canvas.drawText(PrintFont.terminalid+loginRecord.getTerminalId() , paint);
        canvas.drawText(PrintFont.loginDate+ TimeUtils.date3String(loginRecord.getLoginDate()) , paint);
        canvas.drawText(PrintFont.loginTime+TimeUtils.date4String(loginRecord.getLoginDate()) , paint);
        canvas.drawText(PrintFont.logOutDate+TimeUtils.date3String(loginRecord.getLogoutDate()) , paint);
        canvas.drawText(PrintFont.logOutTime+TimeUtils.date4String(loginRecord.getLogoutDate()) , paint);
        canvas.drawText(PrintFont.soldQty+loginRecord.getSoldQty() , paint);
        canvas.drawText(PrintFont.soldAmount+loginRecord.getSoldAmount() , paint);
        canvas.drawText(PrintFont.paymentQty+loginRecord.getPaymentQty() , paint);
        canvas.drawText(PrintFont.paymentAmount+loginRecord.getPaymentAmount() , paint);
        canvas.drawText(PrintFont.netAmount+loginRecord.getNetAmount() , paint);

        ret = printData(canvas);


        ToastUtils.showToast("  "+ret);
        return ret ;
    }
    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }


    public  int printPosRecord(PosRecord posRecord) {
        this.printTask = new PrintTask();
        this.printFlag = true;
        int ret ;
        printer = Printer.getInstance() ;
        if(printer == null){
            return 110 ;
        }
        PrintCanvas canvas = new PrintCanvas() ;
        Paint paint = new Paint() ;

        setFontStyle(paint , 3 , true);
        canvas.drawText("TSN:"+posRecord.getTsn()+"(NO)" , paint);
        setFontStyle(paint , 2 , true);
        canvas.drawText(App.companyName , paint);
        printLine(paint , canvas);
        setFontStyle(paint , 1 , true);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_costomer_logo);

        canvas.drawBitmap(scaleBitmap(bitmap, 0.3F) , paint);
        if(!bitmap.isRecycled()){
            bitmap.recycle();
        }

        printLine(paint , canvas);
        setFontStyle(paint , 2 , false);
        canvas.drawText(App.slogan, paint);
        setFontStyle(paint , 3 , true);
        canvas.drawText("WeekNO:"+1, paint);

        setFontStyle(paint , 2 , true);
        canvas.drawText( printTwoData("TerminalID :",posRecord.getTID()), paint);
        canvas.drawText( printTwoData("MatchPlayed:",TimeUtils.date3String(posRecord.getMatchPlayed())), paint);
        canvas.drawText( printTwoData("ClosingTime:",posRecord.getClosingTime()), paint);
        canvas.drawText( printTwoData("Validity   :",TimeUtils.date3String(posRecord.getValidity())), paint);
        printLine(paint,canvas);
        setFontStyle(paint , 2 , true);
        List<Item> list = JsonUtil.stringToList(posRecord.getList(),Item.class);
        Item item;
        for (int i = 0; i <list.size() ; i++) {
            item = list.get(i);
            canvas.drawText( item.getGameType()+" "+item.getGameOption()+" "+item.getOldType()  + "   ＄ " +item.getAmount(), paint);
            canvas.drawText( item.getList().toString(), paint);
        }
        printLine(paint,canvas);
        printLine(paint,canvas);
        setFontStyle(paint , 2 , true);
        canvas.drawText( "Total Stake:         $" +posRecord.getTotalStake(), paint);
        printLine(paint,canvas);
        setFontStyle(paint , 2 , true);
        canvas.drawText( "TID:" +posRecord.getTID()+"   "+TimeUtils.date5String(posRecord.getCreateTime()), paint);


        try {
            canvas.drawBitmap(CreateOneDCode(posRecord.getTsn()) , paint);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        ret = printData(canvas);
        return ret ;

    }
    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    private static final int LEFT_LENGTH = 20;

    private static final int RIGHT_LENGTH = 12;

    /**
     * 左侧汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 8;

    /**
     * 小票打印菜品的名称，上限调到8个字
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 400, 80);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
