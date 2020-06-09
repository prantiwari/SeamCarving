package Week2;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {
    public static void main(String[] args) {

        Picture inputImg = new Picture("Balcony.jpg");
        int removeColumns = Integer.parseInt("100");



        SeamCarver sc = new SeamCarver(inputImg);

        Stopwatch sw = new Stopwatch();



        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }
        Picture outputImg = sc.picture();


        inputImg.show();
        System.out.println("show");
        outputImg.show();
    }

}
