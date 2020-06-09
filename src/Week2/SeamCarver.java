package Week2;
//Get the height and weight independent of picture

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class SeamCarver {
    private Picture picture;

    private double[][] energy;
    private Bag[][] edges;
    int color[][];

    public SeamCarver(Picture picture) {
        this.picture = picture;
        int height = picture.height();
        int width = picture.width();
        energy = new double[width][height];
        color = new int[width][height];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++)
            {
              color[j][i] = picture.getRGB(j,i);
            }
        }




        for(int i = 0; i < height; i++) {
            energy[0][i] = 1000;
            energy[width-1][i] = 1000;
        }
        for(int i = 1; i < width-1; i++) {
            for(int j = 1; j < height-1; j++)
            {
               energy[i][j] = calcEnergy(i,j);

            }
        }
        for(int i = 0;i < width;i++) {
            energy[i][0] = 1000;
            energy[i][height-1] = 1000;
        }

    }

    public Picture picture() {
        Picture picture = new Picture(width(),height());
        for(int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                picture.setRGB(j,i,color[j][i]);
            }
        }

      return picture;
    }

    private double calcEnergy(int col,int row) {
        int rgbt1 = color[col][row - 1];
        int rgbt2 = color[col][row + 1];
        int rgba1 = color[col - 1][row];
        int rgba2 = color[col + 1][row];
        double dx = Math.pow(((rgba1 >> 16) & 0xFF) -((rgba2 >> 16) & 0xFF), 2) + Math.pow(((rgba1 >> 0) & 0xFF) - ((rgba2 >> 8) & 0xFF), 2) + Math.pow(((rgba1 >> 0) & 0xFF) - ((rgba2 >> 0)& 0xFF), 2);
        double dy = Math.pow(((rgbt1 >> 16) & 0xFF) -((rgbt2 >> 16) & 0xFF), 2) + Math.pow(((rgbt1 >> 0) & 0xFF) - ((rgbt2 >> 8) & 0xFF), 2) + Math.pow(((rgbt1 >> 0) & 0xFF) - ((rgbt2 >> 0)&0xFF), 2);
        return Math.sqrt(dx+dy);
    }

    public     int width()  {
        return color.length;

    }

    public     int height()  {

        return color[0].length;
    }

    public  double energy(int x, int y) {
        return energy[x][y];
    }              // energy of pixel at column x and row y

    public   int[] findVerticalSeam() {
        int size = height() * width() + 2;
        Bag<Integer>[] edges = drawGraph();
        double[] weights = getweights();
        double[] distto = new double[size];
        int[] edgeto = new int[size];
        for(int v = 0; v < size; v++) {
            distto[v] = Double.POSITIVE_INFINITY;}
        distto[0] = 0;
        for(int v = 0; v < edges.length; v++) {
            relax(edges[v], v, weights, distto, edgeto);
        }



        Stack<Integer> path = new Stack<>();
        for(int x = edgeto[size-1]; x != 0; x = edgeto[x])
            path.push(x);

        int[] seam = new int[path.size()];
        for (int v = 0; v < seam.length; v++){
            int l = path.pop();
            seam[v] = getcol(l-1);

        }
    System.out.println(seam.length);
    System.out.println(height());
    System.out.println(width());
      return seam;
    }

    private void relax(Bag<Integer> bag, int v, double[] weights, double[] distto, int[] edgeto) {
        for(int w : bag){
      if (distto[w] > distto[v] + weights[w]) {
        edgeto[w] = v;
        distto[w] = distto[v] + weights[w];
                }
        }
    }



    private double[] getweights() {
        int size = height() * width() + 2;
        double[] weight = new double[size];
        weight[0] = 1000;
        int k = 1;
        for(int i = 0; i < height(); i++) {
            for(int j = 0; j < width(); j++) {
                weight[k] = energy[j][i]; k++;}
      }

      weight[size-1] = 1000;
      return weight;
    }

    private Bag<Integer>[] drawGraph() {
        int size = width() * height() + 2;
        Bag<Integer>[] adj = new Bag[size];
        for(int i = 0; i < size; i++)
            adj[i] = new Bag<>();
        int width = width();
        for(int i = 1; i < size - width - 1; i++) {
          int[] l = {i + width - 1, i + width, i + width + 1};
          int[] m = {i + width, i + width - 1};
          int[] n = {i + width, i + width + 1};
          if( i % width ==1 ) {
              for(int j = 0; j < m.length; j++)
                  adj[i].add(m[j]);

          }
          if (i % width == 0) {
              for(int j = 0; j < n.length; j++)
                  adj[i].add(n[j]);
          }
          else
              for(int j = 0;j < l.length; j++)
                  adj[i].add(l[j]);

        }
        for(int j = 1;j <= width; j++)            adj[0].add(j);
        for(int j = size - width - 1; j < size; j++)
            adj[j].add(size - 1);

        return adj;
    }

    private int getcol(int v) {

        return v % width();
    }
  //  public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        int[][] colorn = new int[width() - 1][height()];
        double[][] energyn = new double[width() - 1][height()];
        int k = 0;
        for(int i = 0; i < height(); i++) {
            for(int j = 0; j < width() - 1; j++){
             if(j >= seam[k])
                 colorn[j][i] = color[j+1][i];

             else
                 colorn[j][i] = color[j][i];
                 } k++;
            }
            color = colorn;
           k = 0;
            for (int i = 0; i < height(); i++) {
                for (int j = 0; j < width(); j++) {
                    if ( i == 0 || j == 0 || i == height() - 1 || j == width() - 1)
                        energyn[j][i] = 1000;
                    else if (j == seam[k] && j > 1 && j < width() - 1) {
            energyn[seam[k]][i] = calcEnergy(seam[k],i);
            energyn[j - 1][i] = calcEnergy(j - 1,i);

                    }
            else
                energyn[j][i] = energy[j][i];
            }
            k++;
        }


    }

    public static void main(String[] args) {
        Picture picture=new Picture("chameleon.png");
        SeamCarver sc=new SeamCarver(picture);
       StdDraw.setYscale(0,picture.height());
        StdDraw.setXscale(0,picture.width());
        int[] seam=sc.findVerticalSeam();
        for(int i=0;i<seam.length;i++){
            StdDraw.point(seam[i],i);
        }
        System.out.println(sc.height());
        System.out.println(seam.length);

    }// remove vertical seam from current picture
}