package utils;

public class RabinKarp {
    private final static int alphabetSize = 256;
    private final static int prime = 137; // some prime number
    private int possibilities;
    private long bodyHash;
    private int bodyLength;

    /** Get the hash value of the pattern, set bodyLength and possibilities
     * @param pattern the String we want to use as our pattern to search for */
    public RabinKarp(String pattern){
        bodyHash = getHash(pattern);
        bodyLength = pattern.length();
        possibilities = (int)Math.pow(alphabetSize, bodyLength-1);
    }
    /** This method checks a String for an instance of pattern by comparing hash values
     * If text.length() >= bodyLength we first calculate the hash value of the first substring of length bodyLength
     * then by removing the leading characters hash value and adding on the trailing characters hash value
     * we keep successive calculations of hash after the first as O(1) */
    public int search(String text){
        int textLength = text.length();
        if(textLength < bodyLength)  return -1;
        long textHash = getHash(text.substring(0,bodyLength));
        if(textHash==bodyHash) return 0;
        for(int i=0;i<=textLength-bodyLength-1;i++){
            if(textHash==bodyHash){
                return i;
            }
            textHash = (alphabetSize*(textHash - text.charAt(i) * possibilities)
                    + text.charAt(i+bodyLength)) % prime;
        }
        return -1;

    }
    private long getHash(String str){
        long result = 0;
        for(int i=0;i<str.length();i++){
            result = (alphabetSize * result + str.charAt(i)) % prime;
        }
        return result;
    }
//                                      MAIN FUNCTION FOR TESTING
//    public static void main(String args[]){
//        String text = "sum_a=0;for(inti=0;i<4;i++)sum_a+=array_a[i];for(inti=0;i<10;i++){temperature++;}intaverage_a=sum_a/4;";
//        String pattern = "sum_a=0;for(inti=0;i<4;i++)sum_a+=array_a[i];for(inti=0;i<10;i++){temperature++;}";
//        RabinKarp rk = new RabinKarp(pattern);
//        int i = rk.search(text);
//        if(i>-1) System.out.println("Pattern found in text at position "+i);
//        else System.out.println("No match "+i);
//    }
}
