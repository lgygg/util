package com.lgy.util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 计算工具类
 */
public class CalculationUtil {
    private CalculationUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 提供精确的加法運算
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供了精确的減法運算
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /***
     * 提供了精确的乘法運算
     * 结果四舍五入保留6位小数
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return double6Point(b1.multiply(b2).doubleValue());
    }

    /**
     * 提供了精确的乘法運算
     * 结果不做处理
     *
     * @param
     */
    public static double mul1(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供了(相对)精确的除法運算，當發生除不儘的情況時，由scale参数指定
     * 精度，以后的數字四捨五入
     *
     * @param
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Double类型转化为四舍五入6位小数
     *
     * @param
     * @return
     */
    public static double double6Point(double num) {

        BigDecimal b = new BigDecimal(String.valueOf(num));
        BigDecimal bg = b.setScale(6, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();

    }

    /**
     * Double类型转化为四舍五入2位小数
     *
     * @param
     * @return
     */
    public static double double2Point2(double num) {

        BigDecimal b = new BigDecimal(String.valueOf(num));
        BigDecimal bg = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();

    }

    /**
     * String类型转化为四舍五入2位小数
     *
     * @param
     * @return
     */
    public static double string2Point2(String num) {

        BigDecimal b = new BigDecimal(String.valueOf(num));
        BigDecimal bg = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();

    }

    /**
     * 去掉小数点后面无用的0
     *
     * @param num
     * @return
     */
    public static String doubleTrans1(double num) {
        if (num % 1.0 == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /**
     * 正数返回绝对值负数
     *
     * @param a
     * @return
     */
    public static double Abs(double a) {
        return ((a > 0) ? -a : a);
    }

    /**
     * 负数返回绝对值正数
     *
     * @param a
     * @return
     */
    public static double Abs1(double a) {
        return ((a < 0) ? -a : a);
    }


    /**
     * 去除科学计算法
     *
     * @param num
     * @return
     */
    public static String doubleTrans2(double num) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(6);
        String is = formatter.format(num);
        return is;
    }


    /**
     * 设置最多6位小数，输入.自动转换为0.
     *
     * @param editText
     */
    public static void setPricePoint1(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
               /* if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 6) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 7);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }*/
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setSelectAllOnFocus(true);
        editText.setFilters(new InputFilter[]{lengthfilter});
    }


    //限制输入小数位数
    public static InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                if (dotValue.length() == 6) {
                    return "";
                }
               /* int diff = dotValue.length() + 5;
                if (diff > 0) {
                    return "";
                }*/
            }
            return null;
        }
    };
}