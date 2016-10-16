# AES-Cipher
An Implementation of the AES-256 cipher algorithm

### Authors:
Brett Blashko
Andrew Wilson
Alice Gibbons

### Compile:
    javac *.java

### Usage:
    java AES <e/d> <keyFilename> <inputFilename>

    <e/d>: [e] Encrypt a file with provided key
           [d] Decrypt a file with provided key

    <keyFilename>: Path to the file containing a hexidecimal
                   key with exactly 64 characters (32 bytes)

    <inputFilename>: Path to the file containg hexadecimal characters
                     to be encrypted or decrypted. Each line must contain
                     exactly 32 hexadecimal characters (16 bytes)
