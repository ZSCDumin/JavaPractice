/**
 * 
 */
package calculator;

import java.awt.Dialog;
import java.math.BigInteger;

import javax.swing.JOptionPane;

/**
 * @author 杜敏
 *
 */
class MathCal {
    //倒数
	public static double daoShu(double num) {
		return 1.0 / num;
	}
    //阶乘
	public static BigInteger jieCheng(int num) {
		BigInteger bigInteger = BigInteger.ONE;
		if (num == 0) {
			return bigInteger = BigInteger.ZERO;
		} else {
			for (long i = 1; i <= num; i++) {
				bigInteger = bigInteger.multiply(BigInteger.valueOf(i));
			}
			return bigInteger;
		}
		
	}
    //sin
	public static double sin(double num) {
		return Math.sin(num);
	}
    //cos
	public static double cos(double num) {
		return Math.cos(num);
	}
    //tan
	public static double tan(double num) {
		return Math.tan(num);
	}
	//log对数
	public static double log(double num) {
		return Math.log(num);
	}
	//根号
	public static double sqrt(double num) {
		return Math.sqrt(num);
	}
	//n次方
	public static double pow (double num1,double num2) {
		return Math.pow(num1, num2);
	}
	//百分数
	public static double percent(double num)
	{
		return num/100;
	}
	//计算角度
	public static double angle(double num)
	{
		return num / 180 * Math.PI;
	}
}
