import java.util.Scanner;

public class PlayReversi {
		
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter XvY where X and Y is either p (person) or c (computer). Agent X is black.");
		String str = scan.nextLine();
		Players players = new Players(str, scan);
		while (true) {
			players.play();
		}
	}

}
