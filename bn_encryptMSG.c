#include <stdio.h>
#include <openssl/bn.h>

#define NBITS 256

void printBN(char *msg, BIGNUM * a){
        char * number_str = BN_bn2hex(a);
        printf("%s %s\n", msg, number_str);
        OPENSSL_free(number_str);
}

void encryptMessage(BIGNUM * m, BIGNUM * n, BIGNUM * e){
       	BIGNUM *res = BN_new();
	BN_CTX *ctx = BN_CTX_new();
	//Calculate public-key encryption:
        BN_mod_exp(res, m, e, n, ctx);

        printBN("C = ", res);

	OPENSSL_free(res);
	OPENSSL_free(ctx);
}

int main(int argc, char *argv[]){
        BIGNUM *m = BN_new();
        BIGNUM *n = BN_new();
        BIGNUM *e = BN_new();

	if (argc < 4){
		printf("Error: need 3 CL-arguments!\n");
		return 0; 
	}

        BN_hex2bn(&m, argv[1]);
        BN_hex2bn(&n, argv[2]);
        BN_hex2bn(&e, argv[3]);

	encryptMessage(m, n, e);

        return 0;
}
