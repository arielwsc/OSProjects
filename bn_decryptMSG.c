#include <stdio.h>
#include <openssl/bn.h>

#define NBITS 256

void printBN(char *msg, BIGNUM * a){
        char * number_str = BN_bn2hex(a);
        printf("%s %s\n", msg, number_str);
        OPENSSL_free(number_str);
}

void decryptMessage(BIGNUM * c, BIGNUM * d, BIGNUM * n){
        BIGNUM *res = BN_new();
        BN_CTX *ctx = BN_CTX_new();
        //Calculate private-key decryption:
        BN_mod_exp(res, c, d, n, ctx);

        printBN("M = ", res);

        OPENSSL_free(res);
        OPENSSL_free(ctx);
}

int main(int argc, char *argv[]){
        BIGNUM *c = BN_new();
        BIGNUM *d = BN_new();
        BIGNUM *n = BN_new();

        if (argc < 4){
                printf("Error: need 3 CL-arguments!\n");
                return 0;
        }

        BN_hex2bn(&c, argv[1]);
        BN_hex2bn(&d, argv[2]);
        BN_hex2bn(&n, argv[3]);

        decryptMessage(c, d, n);

        return 0;
}


