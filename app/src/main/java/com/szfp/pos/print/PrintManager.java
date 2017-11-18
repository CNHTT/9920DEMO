package com.szfp.pos.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;
import com.szfp.pos.App;
import com.szfp.pos.Logger;
import com.szfp.pos.R;
import com.szfp.pos.model.LoginRecord;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.TimeUtils;

import java.text.DecimalFormat;
import java.util.Date;

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

    public int ptintLoginRecord(LoginRecord loginRecord) {
//        this.printTask = new PrintTask();
        this.printFlag = true;
        int ret ;
        printer = Printer.getInstance() ;
        if(printer == null){
            return 110 ;
        }
        PrintCanvas canvas = new PrintCanvas() ;
        Paint paint = new Paint() ;


        setFontStyle(paint , 2 , false);
        canvas.drawText(App.companyName , paint);
        printLine(paint , canvas);
        setFontStyle(paint , 1 , true);
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_costomer_logo);
        canvas.drawBitmap(image , paint);
        if(!image.isRecycled()){
            image.recycle();
        }


        printLine(paint , canvas);
        setFontStyle(paint , 2 , false);
        canvas.drawText(App.slogan, paint);
        canvas.drawText(PrintFont.logoutSummary, paint);
        printLine(paint , canvas);
        setFontStyle(paint , 2 , false);
        canvas.drawText(PrintFont.logoutSummary+loginRecord.getTerminalId() , paint);
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


        return ret ;
    }
}
