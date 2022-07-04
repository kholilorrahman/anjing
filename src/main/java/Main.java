import frame.AnjingViewFrame;
import helpers.koneksi;

public class Main {
    public static void main(String[] args) {
        koneksi.getConnection();
        AnjingViewFrame viewFrame = new AnjingViewFrame();
        viewFrame.setVisible(true);

    }
}
