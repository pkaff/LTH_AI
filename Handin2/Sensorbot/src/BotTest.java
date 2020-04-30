
public class BotTest {

	public static void main(String[] args) { 
		double ACCURACY = 0.99;
		Bot b = new Bot();
		Sensor s = new Sensor();
		while (s.maxProb() < ACCURACY) {
			s.sense(b.move());
		}
		s.printBeliefState();
		b.printTrueLocation();
	}

}
