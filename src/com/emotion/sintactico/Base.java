/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emotion.sintactico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Base {

    private List<List> producciones = new ArrayList();

    public Base() {
        initListas();
    }
    
    public int [][] gramar = {{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,0,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{2,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,1,600,600,600,600,600,600,600,600,600,600,2,600,600,2,600,2,600,600,2,2,600},
{3,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{4,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,7,600,600,600,5,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,79,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,7,600,600,600,5,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,7,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,8,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,9,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,10,11,12,13,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{15,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,15,14,600,600,600,600,600,600,600,600,600,600,15,600,600,15,600,15,600,600,15,15,600},
{16,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,17,600,600,600,600,600,600,600,600,600,600,600,16,600,600,16,600,16,600,600,16,16,600},
{18,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,19,600,600,20,600,21,600,600,22,23,600},
{16,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,25,600,600,600,600,600,600,600,600,600,600,600,16,25,25,16,25,16,25,25,16,16,600},
{26,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{27,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,29,29,29,600,29,28,29,29,29,29,29,29,29,29,29,29,29,29,29,29,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,31,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,32,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,33,34,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,35,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,36,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,37,600,600},
{38,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,39,600,600,40,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,41,600},
{42,600,600,600,42,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,42,600,600,600,600,600,600,600,600,42,42,42,42,42,600,600,600,600,600,600,600,600,600,600,600},
{600,600,43,600,600,44,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{45,600,600,600,45,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,45,600,600,600,600,600,600,600,600,45,45,45,45,45,600,600,600,600,600,600,600,600,600,600,600},
{47,600,47,47,47,47,600,47,600,600,600,600,600,600,600,600,600,600,600,46,600,47,600,600,600,600,600,600,600,600,47,47,47,47,47,600,600,600,600,600,600,600,600,600,600,600},
{48,600,600,600,48,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,48,600,600,600,600,600,600,600,600,48,48,48,48,48,600,600,600,600,600,600,600,600,600,600,600},
{50,600,50,50,50,50,600,50,600,600,600,600,600,600,600,600,600,600,600,50,49,50,600,600,600,600,600,600,600,600,50,50,50,50,50,600,600,600,600,600,600,600,600,600,600,600},
{51,600,600,600,51,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,51,600,600,600,600,600,600,600,600,51,51,51,51,51,600,600,600,600,600,600,600,600,600,600,600},
{53,600,600,600,53,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,52,600,600,600,600,600,600,600,600,53,53,53,53,53,600,600,600,600,600,600,600,600,600,600,600},
{54,600,600,600,54,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,54,54,54,54,54,600,600,600,600,600,600,600,600,600,600,600},
{600,600,56,56,600,56,600,56,600,600,600,600,600,55,55,55,55,55,55,56,56,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{600,600,600,600,600,600,600,600,600,600,600,600,600,57,59,61,60,62,58,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600},
{63,600,600,600,63,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,63,63,63,63,63,600,600,600,600,600,600,600,600,600,600,600},
{66,600,66,66,66,66,600,66,64,65,600,600,600,66,66,66,66,66,66,66,66,600,600,600,600,600,600,600,600,600,66,66,66,66,66,600,600,600,600,600,600,600,600,600,600,600},
{67,600,600,600,67,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,67,67,67,67,67,600,600,600,600,600,600,600,600,600,600,600},
{71,600,71,71,71,71,600,71,71,71,68,69,70,71,71,71,71,71,71,71,71,600,600,600,600,600,600,600,600,600,71,71,71,71,71,600,600,600,600,600,600,600,600,600,600,600},
{72,600,600,600,78,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,600,73,74,75,76,77,600,600,600,600,600,600,600,600,600,600,600}};
    
    private void initListas() {
//<PROGRAM>  → class (id) <DECLARA> <ESTATUTOS> endclass
List <Integer> prod0 = Arrays.asList(100,122,101,900,123,1,9,100);
producciones.add(prod0);
//<DECLARA> → declare <B>  of  <TIPO> ; <AUX>   111
List <Integer> prod1 = Arrays.asList(100,2,100,7,902,126,8);
producciones.add(prod1);
//<DECLARA> → €     
List <Integer> prod2 = Arrays.asList();
producciones.add(prod2);
//<B> → <ID_DIM> <AUX2>
List <Integer> prod3 = Arrays.asList(3,6);
producciones.add(prod3);
//<ID_DIM> → id <C>
List <Integer> prod4 = Arrays.asList(101,901,4);
producciones.add(prod4);
//<C> → [cteentera] <AUX3>     
List <Integer> prod5 = Arrays.asList(124,102,125,5); //AQUI NO SE QUE ACCION SEMANTICA VA
producciones.add(prod5);
//<AUX3> → <C>
List <Integer> prod6 = Arrays.asList(6); //AQUI TAMPOCO PORQUE TODO ESTO ES DE LAS DIMENSIONADAS ME PARECE
producciones.add(prod6);
//<AUX3> → €     
List <Integer> prod7 = Arrays.asList();
producciones.add(prod7);
//<AUX2> → <B>   
List <Integer> prod8= Arrays.asList(127,2);
producciones.add(prod8);
//<AUX2> → €     
List <Integer> prod9= Arrays.asList();
producciones.add(prod9);
//<TIPO> →  int
List <Integer> prod10 = Arrays.asList(100);
producciones.add(prod10);
//12.	<TIPO> →  float (11)
List <Integer> prod11= Arrays.asList(100);
producciones.add(prod11);
//13.	<TIPO> →  carácter  (12)
List <Integer> prod12= Arrays.asList(100);
producciones.add(prod12);
//14.	<TIPO> →  string  (13)
List <Integer> prod13 = Arrays.asList(100);
producciones.add(prod13);
//15.	<AUX> →  <DECLARA>  (14)
List <Integer> prod14 = Arrays.asList(1);
producciones.add(prod14);
//16.	<AUX> → €  (15)
List <Integer> prod15 = Arrays.asList();
producciones.add(prod15);
//17.	<ESTATUTOS> →  <E> ; <AUX4> (16)
List <Integer> prod16 = Arrays.asList(10,126,11);
producciones.add(prod16);
//18.	<ESTATUTOS> →  €  (17)
List <Integer> prod17 = Arrays.asList();
producciones.add(prod17);
//19.	<E> →  <EST_ASIG> (18)
List <Integer> prod18 = Arrays.asList(12);
producciones.add(prod18);
//20.	<E> →  <EST_IF> (19)
List <Integer> prod19 = Arrays.asList(16);
producciones.add(prod19);
//21.	<E> →  <EST_WHILE> (20)
List <Integer> prod20 = Arrays.asList(18);
producciones.add(prod20);
//22.	<E> →  <EST_DO> (21)
List <Integer> prod21 = Arrays.asList(19);
producciones.add(prod21);
//23.	<E> →  <EST_READ> (22)
List <Integer> prod22 = Arrays.asList(20);
producciones.add(prod22);
//24.	<E> →  <EST_WRITE>  (23)
List <Integer> prod23 = Arrays.asList(23);
producciones.add(prod23);
//25.	<AUX4> →  <ESTATUTOS> (24)
List <Integer> prod24 = Arrays.asList(9);
producciones.add(prod24);
//26.	<AUX4> →  € (25)
List <Integer> prod25 = Arrays.asList();
producciones.add(prod25);
//27.	<EST_ASIG> →  <ASIG> = <EXPR> (26)
List <Integer> prod26 = Arrays.asList(13,109,926,26,927);
producciones.add(prod26);
//28.	<ASIG> →  id <DIM_ASIG> (27)
List <Integer> prod27 = Arrays.asList(101,925,14); // verificar accion para variables dimensionadas
producciones.add(prod27);
//29.	<DIM_ASIG> →  [<EXPR> <AUX5>] (28)
List <Integer> prod28 = Arrays.asList(124,26,15,125); //NO SUPE COMO HACER DIMENSIONADAS
producciones.add(prod28);
//30.	<DIM_ASIG> →  € (29)
List <Integer> prod29 = Arrays.asList();
producciones.add(prod29);
//31.	<AUX5> →  ,  <EXPR> <AUX5> (30)
List <Integer> prod30 = Arrays.asList(127,26,15);
producciones.add(prod30);
//32.	<AUX5> →  €  (31)
List <Integer> prod31 = Arrays.asList();
producciones.add(prod31);
//33.	<EST_IF> →  if(<EXPR>) <ESTATUTOS> <H> endif (32)
List <Integer> prod32 = Arrays.asList(100,122,26,928,123,9,17,100,930);
producciones.add(prod32);
//34.	<H> →  else <ESTATUTOS> (33)
List <Integer> prod33 = Arrays.asList(100,929,9);
producciones.add(prod33);
//35.	<H> →  € (34)
List <Integer> prod34 = Arrays.asList();
producciones.add(prod34);
//36.	<EST_WHILE> →  while (<EXPR>) <ESTATUTOS> endwhile (35)
List <Integer> prod35 = Arrays.asList(100,938,122,26,123,931,9,100,932);
producciones.add(prod35);
//37.	<EST_DO> → do <ESTATUTOS> dowhile (<EXPR>) enddo (36)
List <Integer> prod36 = Arrays.asList(100,933,9,100,122,26,934,123,100,935);
producciones.add(prod36);
//38.	<EST_READ> →  read (<I>) (37)
List <Integer> prod37 = Arrays.asList(100,122,21,123,937);
producciones.add(prod37);
//39.	<I> →  id <AUX6> (38)
List <Integer> prod38 = Arrays.asList(101,936,22);
producciones.add(prod38);
//40.	<AUX6> →  , <I>  (39)
List <Integer> prod39 = Arrays.asList(127,21);
producciones.add(prod39);
//41.	<AUX6> →  €  (40)
List <Integer> prod40 = Arrays.asList();
producciones.add(prod40);
//42.	<EST_WRITE> →  write (<J>)  (41)
List <Integer> prod41 = Arrays.asList(100,122,24,123);
producciones.add(prod41);
//43.	<J> →  <EXPR> <AUX7>  (42)
List <Integer> prod42 = Arrays.asList(26,939,25);
producciones.add(prod42);
//44.	<AUX7> →  , <J>  (43)
List <Integer> prod43 = Arrays.asList(127,24);
producciones.add(prod43);
//45.	<AUX7> →  €  (44)
List <Integer> prod44 = Arrays.asList();
producciones.add(prod44);
//46.	<EXPR> →  <EXPR2> <AUX8> (45)
List <Integer> prod45 = Arrays.asList(28,923,27);
producciones.add(prod45);
//47.	<AUX8> →  || <EXPR> (46)
List <Integer> prod46 = Arrays.asList(105,924,26); //AQUI NO SE SI DEBO PONER DE NUEVO LA ACCION DE ARRIBA LA DE  AL LADO DE EXPR2
producciones.add(prod46);
//48.	<AUX8> →  € (47)
List <Integer> prod47 = Arrays.asList();
producciones.add(prod47);
//49.	<EXPR2> →  <EXPR3> <AUX9> (48)
List <Integer> prod48 = Arrays.asList(30,921,29);
producciones.add(prod48);
//50.	<AUX9> →  && <EXPR2> (49)
List <Integer> prod49 = Arrays.asList(117,922,28); //AQUI NO SE SI DEBO PONER DE NUEVO LACCION DE ARRIBA LA DEL AL LADO DE EXPR3
producciones.add(prod49);
//51.	<AUX9> →  € (50)
List <Integer> prod50 = Arrays.asList();
producciones.add(prod50);
//52.	<EXPR3> →  <NOT> <EXPR4> (51)
List <Integer> prod51 = Arrays.asList(31,32,920);
producciones.add(prod51);
//53.	<NOT> → ! (52)
List <Integer> prod52 = Arrays.asList(115,919);
producciones.add(prod52);
//54.	<NOT> → € (53)
List <Integer> prod53 = Arrays.asList();
producciones.add(prod53);
//55.	<EXPR4> →  <EXPR5> <M> (54)
List <Integer> prod54 = Arrays.asList(35,33);
producciones.add(prod54);
//56.	<M> →  <OPREL> <EXPR5> (55)
List <Integer> prod55 = Arrays.asList(34,35,918);
producciones.add(prod55);
//57.	<M> → € (56)
List <Integer> prod56 = Arrays.asList();
producciones.add(prod56);
//58.	<OPREL> → ==  (57)
List <Integer> prod57 = Arrays.asList(110,917);
producciones.add(prod57);
//59.	<OPREL> → !=  (58)
List <Integer> prod58 = Arrays.asList(116,917);
producciones.add(prod58);
//60.	<OPREL> → <  (59)
List <Integer> prod59 = Arrays.asList(111,917);
producciones.add(prod59);
//61.	<OPREL> → <=  (60)
List <Integer> prod60 = Arrays.asList(112,917);
producciones.add(prod60);
//62.	<OPREL> → >  (61)
List <Integer> prod61 = Arrays.asList(113,917);
producciones.add(prod61);
//63.	<OPREL> → >=  (62)
List <Integer> prod62 = Arrays.asList(114,917);
producciones.add(prod62);
//64.	<EXPR5> →  <TERM> <AUX10> (63)
List <Integer> prod63 = Arrays.asList(37,914,36);
producciones.add(prod63);
//65.	<AUX10> →  + <EXPR5>  (64)
List <Integer> prod64 = Arrays.asList(118,915,35);
producciones.add(prod64);
//66.	<AUX10> → - <EXPR5>  (65)
List <Integer> prod65 = Arrays.asList(119,916,35);
producciones.add(prod65);
//67.	<AUX10> →  €  (66)
List <Integer> prod66 = Arrays.asList();
producciones.add(prod66);
//68.	<TERM> →  <FACT> <AUX11>  (67)
List <Integer> prod67 = Arrays.asList(39,910,38);
producciones.add(prod67);
//69.	<AUX11> →  * <TERM>  (68)
List <Integer> prod68 = Arrays.asList(120,911,37);
producciones.add(prod68);
//70.	<AUX11> →  / <TERM>   (69)
List <Integer> prod69 = Arrays.asList(121,912,37);
producciones.add(prod69);
//71.	<AUX11> →  % <TERM>  (70)
List <Integer> prod70 = Arrays.asList(128,913,37);
producciones.add(prod70);
//72.	<AUX11> →  €  (71)
List <Integer> prod71 = Arrays.asList();
producciones.add(prod71);
//73.	<FACT> →  <ASIG>  (72)
List <Integer> prod72 = Arrays.asList(13);
producciones.add(prod72);
//74.	<FACT> →  cteentera  (73)
List <Integer> prod73 = Arrays.asList(102,903);
producciones.add(prod73);
//75.	<FACT> →  ctereal  (74)
List <Integer> prod74 = Arrays.asList(103,904);
producciones.add(prod74);
//76.	<FACT> →  ctenotacion  (75)
List <Integer> prod75 = Arrays.asList(104,905);
producciones.add(prod75);
//77.	<FACT> →  ctecaracter  (76)
List <Integer> prod76 = Arrays.asList(106,906);
producciones.add(prod76);
//78.	<FACT> →  ctestring  (77)
List <Integer> prod77 = Arrays.asList(107,907);
producciones.add(prod77);
//79.	<FACT> →  ( <EXPR> )  (78)
List <Integer> prod78 = Arrays.asList(122,908,26,123,909);
producciones.add(prod78);
//80.	<C> →  €   (80)
List <Integer> prod79 = Arrays.asList();
producciones.add(prod79);
    }

    public List getPoduccionesByIndex(int index){
        return producciones.get(index);
    }
       
    public int getColumnByToken(int lexema, String token){
        int column = 600;
        switch(lexema){
            case 100:
                switch(token){
                    case "class":
                        column = 22;
                        break;
                    case "endclass":
                        column = 23;
                        break;
                    case "int":
                        column = 26;
                        break;
                    case "float":
                        column = 27;
                        break;
                    case "char":
                        column = 28;
                        break;
                    case "string":
                        column = 29;
                        break;
                    case "declare":
                        column = 24;
                        break;
                    case "of":
                        column = 25;
                        break;
                    case "if":
                        column = 35;
                        break;
                    case "else":
                        column = 36;
                        break;
                    case "endif":
                        column = 37;
                        break;
                    case "while":
                        column = 38;
                        break;
                    case "endwhile":
                        column = 39;
                        break;
                    case "do":
                        column = 40;
                        break;
                    case "dowhile":
                        column = 41;
                        break;
                    case "enddo":
                        column = 42;
                        break;
                    case "read":
                        column = 43;
                        break;
                    case "write":
                        column = 44;
                        break;
                    default:
                        column = 600;
                        break;
                }
                break;
            case 101:
                column = 0;
                break;
            case 102:
                column = 30;
                break;
            case 103:
                column = 31;
                break;
            case 104:
                column = 32;
                break;
            case 105:
                column = 19;
                break;
            case 106:
                column = 33;
                break;
            case 107:
                column = 34;
                break;
            case 109:
                column = 1;
                break;
            case 110:
                column = 13;
                break;
            case 111:
                column = 14;
                break;
            case 112:
                column = 16;
                break;
            case 113:
                column = 15;
                break;
            case 114:
                column = 17;
                break;
            case 115:
                column = 21;
                break;
            case 116:
                column = 18;
                break;
            case 117:
                column = 20;
                break;
            case 118:
                column = 8;
                break;
            case 119:
                column = 9;
                break;
            case 120:
                column = 10;
                break;
            case 121:
                column = 11;
                break;
            case 122:
                column = 4;
                break;
            case 123:
                column = 5;
                break;
            case 124:
                column = 6;
                break;
            case 125:
                column = 7;
                break;
            case 126:
                column = 3;
                break;
            case 127:
                column = 2;
                break;
            case 128:
                column = 12;
                break;
            default:
                column = 600;
                break;
        }
        return column;
    }
}
