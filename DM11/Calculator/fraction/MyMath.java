/**
 * 
 */
package fraction;

/**
 * @author 杜敏
 *
 */
public class MyMath {
	// 分数相加
	public static Fraction fracAdd(int first_numerator, int first_denominator, int second_numrator,
			int second_denominator) {
		// 以下代码能够在控制台上显示结果
		// 需要调用求最大公约数的函数
		// 需要调用求最小公倍数的函数
		int a = lcm(first_denominator, second_denominator);
		int numerator, denominator;
		numerator = first_numerator * (a / first_denominator) + second_numrator * (a / second_denominator);
		denominator = a;
		int b = gcd(numerator, denominator);
		numerator = numerator / b;
		denominator = denominator / b;
		Fraction fraction=new Fraction();
		fraction.setFezi(numerator);
		fraction.setFenmu(denominator);
		return fraction;
	}

	// 分数相减
	public static Fraction fracSub(int first_numerator, int first_denominator, int second_numrator,
			int second_denominator) {
		int a = lcm(first_denominator, second_denominator);
		int numerator, denominator;
		numerator = first_numerator * (a / first_denominator) - second_numrator * (a / second_denominator);
		denominator = a;
		int b = gcd(numerator, denominator);
		numerator = numerator / b;
		denominator = denominator / b;
		Fraction fraction=new Fraction();
		fraction.setFezi(numerator);
		fraction.setFenmu(denominator);
		return fraction;
	}

	// 分数相乘
	public static Fraction fracMul(int first_numerator, int first_denominator, int second_numrator,
			int second_denominator) {
		int numerator, denominator;
		numerator = first_numerator * second_numrator;
		denominator = first_denominator * second_denominator;
		int b = gcd(numerator, denominator);
		numerator = numerator / b;
		denominator = denominator / b;
		Fraction fraction=new Fraction();
		fraction.setFezi(numerator);
		fraction.setFenmu(denominator);
		return fraction;
	}

	// 分数相除
	public static Fraction fractDiv(int first_numerator, int first_denominator, int second_numrator,
			int second_denominator) {
		int numerator, denominator;
		numerator = first_numerator * second_denominator;
		denominator = first_denominator * second_numrator;
		int b = gcd(numerator, denominator);
		numerator = numerator / b;
		denominator = denominator / b;
		Fraction fraction=new Fraction();
		fraction.setFezi(numerator);
		fraction.setFenmu(denominator);
		return fraction;
	}

	// 最大公约数
	public static int gcd(int a, int b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}

	// 最小公倍数
	public static int lcm(int m, int n) {
		int b = gcd(m, n) * (m / gcd(m, n)) * (n / gcd(m, n));
		return b;
	}
}
