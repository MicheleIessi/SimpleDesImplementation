package DES.SimplifiedDES.DifferentialCryptoanalysis;

import DES.SimplifiedDES.SimplifiedDes;
import Matrices.SimplifiedDES.SimplifiedExpansion;
import Matrices.SimplifiedDES.SimplifiedSubstitution;

import java.util.*;

/**
 * Created by Michele Iessi on 07/06/17.
 */
public class ThreeRoundsDifferentialCriptoanalysis {

    private final int MESSAGE_LENGTH = 12;
    private final int MESSAGE_NUMBER = 5000;
    private ArrayList<String> plaintextLeftTable;
    private ArrayList<String> plaintextRightTable;
    private ArrayList<String> ciphertextLeftTable;
    private ArrayList<String> ciphertextRightTable;
    private HashMap<String, Integer> probableLeftKey;
    private HashMap<String, Integer> probableRightKey;
    private StringBuilder messageGenerator;
    private SimplifiedExpansion simplifiedExpansion;
    private SimplifiedSubstitution simplifiedSubstitution;
    private int rounds;


    public ThreeRoundsDifferentialCriptoanalysis(int rnds) throws NoSuchFieldException, IllegalAccessException {
        rounds = rnds;
        plaintextLeftTable = new ArrayList<>();
        plaintextRightTable = new ArrayList<>();
        ciphertextLeftTable = new ArrayList<>();
        ciphertextRightTable = new ArrayList<>();
        probableLeftKey = new HashMap<>();
        probableRightKey = new HashMap<>();
        messageGenerator = new StringBuilder();
        simplifiedExpansion = new SimplifiedExpansion();
        simplifiedSubstitution = new SimplifiedSubstitution();
        generateRandomMessages();
    }

    /**
     * Questa funzione genera stringhe casuali binarie di 12 bit e le inserisce nella plaintextLeftTable
     */
    private void generateRandomMessages() {

        for(int i=0; i<MESSAGE_NUMBER; i++) {
            plaintextLeftTable.add(generateRandom6BitMessage());
            plaintextRightTable.add(generateRandom6BitMessage());
        }

    }

    public void applyDifferential(SimplifiedDes sDes) throws NoSuchFieldException, IllegalAccessException {

        for(int i=0; i<MESSAGE_NUMBER; i++) {

            String cipheredMessage = sDes.encrypt12Bits(plaintextLeftTable.get(i) + plaintextRightTable.get(i));
            ciphertextLeftTable.add(cipheredMessage.substring(0,6));
            ciphertextRightTable.add(cipheredMessage.substring(6));
        }

        for(int i=0; i<MESSAGE_NUMBER; i++) {

            String R1 = plaintextRightTable.get(0);
            String L1 = plaintextLeftTable.get(i);
            String L4 = ciphertextLeftTable.get(i);
            String R4 = ciphertextRightTable.get(i);
            String L1S = "101110";
            String cipheredS = sDes.encrypt12Bits(L1S+R1);

            String L4S = cipheredS.substring(0,6);
            String R4S = cipheredS.substring(6,12);

            String expandedL4 = simplifiedExpansion.expand(L4);
            String expandedL4S = simplifiedExpansion.expand(L4S);
            String XORL = XOR(expandedL4, expandedL4S);

            String inputXOR1 = XORL.substring(0,4);
            String inputXOR2 = XORL.substring(4);

            String R4First = XOR(R4, R4S);
            String L1First = XOR(L1, L1S);

            String outputXOR = XOR(R4First,L1First);

            String outputS1 = addPadding(outputXOR.substring(0,3));
            String outputS2 = addPadding(outputXOR.substring(3,6));

            List<String> s1 = findAllXORs(inputXOR1, outputS1,1);
            List<String> s2 = findAllXORs(inputXOR2, outputS2,2);

            processProbability(s1, "left", expandedL4);
            processProbability(s2, "right", expandedL4);

        }


        for (Map.Entry<String, Integer> leftEntry : probableLeftKey.entrySet()) {
            for (Map.Entry<String, Integer> rightEntry : probableRightKey.entrySet()) {

                String firstProbableKey = leftEntry.getKey() + rightEntry.getKey();
                for(int i=0; i<2; i++) {
                    String plainTestMessage = plaintextLeftTable.get(i) + plaintextRightTable.get(i);
                    String cipheredTestMessage = ciphertextLeftTable.get(i) + plaintextRightTable.get(i);
                    String probableKey = firstProbableKey + Integer.toString(i);
                    probableKey = probableKey.substring(probableKey.length()-rounds,probableKey.length()) + probableKey.substring(0, probableKey.length()-rounds);

                    SimplifiedDes testSDes = new SimplifiedDes(3, probableKey);
                    //System.out.println("Provo la chiave: " + probableKey + ": il testo cifrato in precedenza è " + cipheredTestMessage + " e il nuovo è " + sDes.encrypt12Bits(plainTestMessage));
                    if(testSDes.encrypt12Bits(plainTestMessage).equals(sDes.encrypt12Bits(plainTestMessage))) {
                        System.out.println("La chiave trovata è " + probableKey);
                        System.exit(0);
                    }
                }
            }

        }
    }


    private void processProbability(List<String> s, String part, String EL4) {

        Map<String, Integer> sMap;
        if(part.equals("left")) {
            sMap = probableLeftKey;
            //System.out.println("Processo la probabilità per la parte sinistra");
        }
        else {
            sMap = probableRightKey;
            //System.out.println("Processo la probabilità per la parte destra");
        }

        String firstL4 = EL4.substring(0,4);
        String lastL4 = EL4.substring(4);

        for(String str : s) {
            String firstString = str.substring(0,4);
            String secondString = str.substring(5);

            String firstXORString = XOR(firstString, firstL4);
            String lastXORString = XOR(secondString, lastL4);
//            System.out.println("prima parte: " + firstXORString);
//            System.out.println("seconda parte: " + lastXORString);
            sMap.putIfAbsent(firstXORString, 1);
            sMap.putIfAbsent(lastXORString, 1);
            if(sMap.containsKey(firstXORString)) {
                int newValue = sMap.get(firstXORString)+1;
                sMap.put(firstXORString, newValue);
            }
            if(sMap.containsKey(lastXORString)) {
                int newValue = sMap.get(lastXORString)+1;
                sMap.put(lastXORString, newValue);
            }
        }
    }

    private void remove0Values() {
        Iterator it = probableLeftKey.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();

            if((int) entry.getValue() == 0) {
                it.remove();
            }
        }

        it = probableRightKey.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();

            if((int) entry.getValue() == 0) {
                it.remove();
            }
        }

    }

    private String generateRandom6BitMessage() {
        messageGenerator = new StringBuilder();
        for(int i=0; i<6; i++) {
            int bit = ((int) (Math.random()*10)) % 2;
            String bitToAdd = String.valueOf(bit);
            messageGenerator.append(bitToAdd);
        }
        return messageGenerator.toString();
    }

    public List<String> findAllXORs(String XORString, String valueToFind, int sNumber) throws NoSuchFieldException, IllegalAccessException {

        List<String> xorList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        // Trovo tutte le coppie di stringhe di 4 bit il cui XOR è uguale a XORString
        for(int i=0; i<16; i++) {

            String integerI = addPadding(Integer.toBinaryString(i));
            for(int j=0; j<16; j++) {

                String integerJ = addPadding(Integer.toBinaryString(j));

                if(XOR(integerI, integerJ).equals(XORString)) {
                    //System.out.println("La coppia " + integerI + "," + integerJ + " rida " + XORString);
                    xorList.add(integerI + "," + integerJ);
                }
            }
        }

        int[][] S = simplifiedSubstitution.getS(sNumber);

        for(String couple : xorList) {

            String firstString = couple.substring(0,4);
            String secondString = couple.substring(5);

            int rowI = Integer.parseInt(String.valueOf(firstString.charAt(0)),2);
            int columnI = Integer.parseInt(String.valueOf(firstString.substring(1)),2);

            int rowJ = Integer.parseInt(String.valueOf(secondString.charAt(0)),2);
            int columnJ = Integer.parseInt(String.valueOf(secondString.substring(1)),2);

            int firstValue = S[rowI][columnI];
            int secondValue = S[rowJ][columnJ];

            String valueFound = addPadding(Integer.toBinaryString(firstValue ^ secondValue));

            if(valueFound.equals(valueToFind)) {

                resultList.add(couple);
                //System.out.println("La coppia " + firstString + "," + firstString + " rida " + valueToFind);
            }
        }
        return resultList;
    }

    private String XOR(String firstString, String secondString) {

        StringBuilder XORString = new StringBuilder();

        for(int i=0; i<firstString.length(); i++) {
            if(firstString.charAt(i) == secondString.charAt(i)) {
                XORString.append("0");
            }
            else {
                XORString.append("1");
            }
        }

        return XORString.toString();
    }


    private String addPadding(String plain) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4 - plain.length(); i++) {
            sb.append("0");
        }
        sb.append(plain);
        return sb.toString();
    }

}
