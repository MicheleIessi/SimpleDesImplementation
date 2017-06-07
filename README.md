# SimpleDesImplementation

## Introduction

A simple Java implementation for the DES/TDES encryption algorithm (ECB mode). 
Done for a mini-project for the course "Combinatorics and Cryptography" for University of L'Aquila.

It has been implemented working with binary strings instead of integers. Later work may involve
modifying the code to suit better the DES specifics and have better performance overall.

Differential Cryptoanalysis[1][2] is used to crack a simplified version of DES, which uses 9 bits keys
and operates in three rounds, as for [2].

Javadoc (partial) and comments are in Italian.


## References
* [1] Eli Biham, Adi Shamir: Differential Cryptanalysis of DES-like Cryptosystems. CRYPTO 1990: 2-21
* [2] Eli Biham: Tutorial on Differenetial Cryptanalysis, 2005
* [3] Wade Trappe, Lawrence C. Washington: Introduction to Cryptography with Coding Theor, Second Edition,
2006: 118-123
