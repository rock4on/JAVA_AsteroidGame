package pachet;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;


//clasa care implementeaza sistemul de input
class MKeyListener extends KeyAdapter{


    //variabila de output pt sistemul de input
    public int a=0;




    //event care verifica daca au fost apasate sagetile
    @Override
    public void keyPressed(KeyEvent event) {

        char ch = event.getKeyChar();
        a=0;


        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {

            System.out.println("Key codes: " + a);
            a=-1;
        }
        else if(event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            System.out.println("Key codes: " + a);
            a=1;
        }
        else{
            System.out.println("Key codes: " + a);
            a=0;
        }
    }
}


public class Principal extends JFrame {

    private JPanel panel1;
    private JTextArea Text;
    private JLabel Scor;
    private JLabel Hscore;

    int Size_x=12;
     int Size_y=46;
     int Highscore=0;
     int pl_poz_y=10;

    private MKeyListener m;

    private char MatriceJoc[][];
    private Vector<Integer> asteroid_x;
    private Vector<Integer> asteroid_y;
    private String imagine()
    {
        String aux=new String();
        for(int i=0;i<Size_x-1;i++)
        {
            for(int j=0;j<Size_y;j++)
            {
               aux+= MatriceJoc[i][j];
            }
            aux+='\n';
        }
        return aux;
    }


    //viteza jocului
    int sp=5000;
    int SP=45000000;
    int viata=3;

    //viteza asteroid
    int asteroid_speed=20;
    int As=10;

    //variabila pt scor
    int score=0;


    public void loop() throws IOException {

        //variabile auxiliare pentru input
        int olda=0;
        int nr_repetitie=0;

        //initializare vector de asteroizi
        asteroid_x=new Vector<Integer>();
        asteroid_y=new Vector<Integer>();

        Path path = Paths.get("scor.txt");
        if(Files.isReadable(path)) {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream("scor.txt"), "UTF-8");

            BufferedReader bufReader = new BufferedReader(reader);

            Highscore = Integer.parseInt(bufReader.readLine());

            reader.close();
        }
        else
        {
            Highscore=0;
        }



        Hscore.setText("Record:"+Integer.toString(Highscore));

        //gameloop
        while(this.isEnabled())
        {
            if(sp==0) {


                //logica pentru asteroizi
                for(int i=0;i<asteroid_y.size();i++)
                {
                    if(asteroid_x.elementAt(i)-1!=0)
                        //stergere asteroid
                    MatriceJoc[asteroid_x.elementAt(i)-1][asteroid_y.elementAt(i)]=' ';
                        //desenare asteroid
                    MatriceJoc[asteroid_x.elementAt(i)][asteroid_y.elementAt(i)]='¤';


                    //loss condition si reset
                    if(asteroid_x.elementAt(i)==Size_x-2 && asteroid_y.elementAt(i)==pl_poz_y)
                    {
                        viata--;
                        if(viata==0) {//afiseaza massagebox
                            if (Highscore >= score) {
                                JOptionPane.showMessageDialog(null, "Ai pierdut", "Ai pierdut", JOptionPane.INFORMATION_MESSAGE);
                            } else {

                                var file = new FileOutputStream("scor.txt");
                                OutputStreamWriter writer = new OutputStreamWriter(
                                        file, "UTF-8");
                                writer.write(Integer.toString(score));
                                writer.close();
                                file.close();


                                Highscore = score;
                                Hscore.setText("Record:" + Integer.toString(score));
                                JOptionPane.showMessageDialog(null, "Ti-ai batut Scorul, Noul scor:" + Integer.toString(score), "Ai pierdut", JOptionPane.INFORMATION_MESSAGE);
                            }
                            //restartare scor
                            score = 0;
                            viata=3;
                            //restartare asteroizi
                            asteroid_y.clear();
                            asteroid_x.clear();

                            //curatare ecran
                            for (int k = 0; k < Size_x; k++) {
                                for (int j = 0; j < Size_y; j++) {
                                    MatriceJoc[k][j] = ' ';
                                    //'█';
                                }
                            }

                        }
                    }
                }




                //desenare player
                MatriceJoc[Size_x - 2][pl_poz_y] = '╬';

                //logica input
                int mv = m.a;
                if (olda == mv) nr_repetitie++;
                else if (nr_repetitie == 1) {
                    mv = 0;
                    nr_repetitie = 0;
                }
                olda = mv;




                //updatare pozitie player
                pl_poz_y -= mv;
                if (pl_poz_y < 0) {
                    pl_poz_y = 0;
                }
                if (pl_poz_y > Size_y - 1) {
                    pl_poz_y = Size_y - 1;
                }



                //deseneaza imaginea
                Text.setText(imagine());

                //curatare pozitia precedenta player
                MatriceJoc[Size_x - 2][pl_poz_y + mv] = ' ';


                //resetare gamespeed
                sp=SP;


                //vector pentru salvarea asteroizilor care trebuie stersi
                Vector<Integer> to_del=new Vector<Integer>();


                //logica asteroizi
                if(asteroid_speed==0) {

                    //initializarea unui asteroid nou pe linia 0 si pe o coloana aleatoare
                    asteroid_x.add(0);
                    asteroid_y.add(new Random().nextInt(Size_y));

                    //miscare asteroid si adaugare in vectorul de stergere daca depaseste fereastra
                    for (int i = 0; i < asteroid_x.size(); i++) {
                        asteroid_x.set(i, asteroid_x.elementAt(i) + 1);
                        if (asteroid_x.elementAt(i) >= Size_x) {
                            to_del.add(i);

                        }
                    }

                    //stergerea asteroidului
                    for (int i = 0; i < to_del.size(); i++) {
                        asteroid_y.removeElementAt(to_del.elementAt(i));
                        asteroid_x.removeElementAt(to_del.elementAt(i));
                        score++;
                    }
                    //resetare viteza asteroid
                    asteroid_speed=As;
                }
                else{

                    asteroid_speed--;
                }
                //setare scor
                Scor.setText(Integer.toString(score));

            }
            else{
                sp--;
            }
        }
    }

    int[]a;

    public static void main(String[] args) throws IOException {
        //initializare fereastra
        Principal f = new Principal();
        f.setContentPane(f.panel1);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();

        //declarare spatiu joc
        f.MatriceJoc = new char[f.Size_x][f.Size_y];



        //initializare spatiu joc
        for(int i=0;i<f.Size_x;i++)
        {
            for(int j=0;j<f.Size_y;j++)
            {
                f.MatriceJoc[i][j]=' ';
                        //'█';
            }
        }

        //desenare player

        f.MatriceJoc[f.Size_x-1][f.pl_poz_y]='╬';//'┼';

        //declarare input
        f.m=new MKeyListener();
        f.Text.addKeyListener(f.m);


        f.setVisible(true);


        //activeaza functia gameloop

            f.loop();



    }

}
