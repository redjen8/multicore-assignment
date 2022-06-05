#include <stdio.h>
#include <omp.h>

int isPrime(int num) {
    if (num <= 1) return 0;
    for (int i = 2; i < num; i++) {
        if (num % i == 0) return 0;
    }
    return 1;
}

int main(int argc, char *argv[]) {
    int scheduleType = atoi(argv[1]);
    int threadNum = atoi(argv[2]);
    int primeCnt = 0;
    printf("schedule type : %d, thread Number : %d\n", scheduleType, threadNum);
    omp_set_num_threads(threadNum);
    double start_time = omp_get_wtime(); 
    switch (scheduleType)
    {
        case 1:
            // static with default chunk size
            #pragma omp parallel for schedule(static) shared(primeCnt)
            for (int i = 1; i<=200000; i++)
            {
                if (isPrime(i)) {
                    #pragma omp critical
                    primeCnt++;
                }
            }
            break;
        case 2:
            // dynamic with default chunk size
            #pragma omp parallel for schedule(dynamic) shared(primeCnt)
            for (int i = 1; i<=200000; i++)
            {
                if (isPrime(i)) {
                    #pragma omp critical
                    primeCnt++;
                }
            }
            break;
        case 3:
            // static with chunk size 10
            #pragma omp parallel for schedule(static, 10) shared(primeCnt) 
            for (int i = 1; i<=200000; i++)
            {
                if (isPrime(i)) {
                    #pragma omp critical
                    primeCnt++;
                }
            }
            break;
        case 4:
            // dynamic with chunk size 10
            #pragma omp parallel for schedule(dynamic, 10) shared(primeCnt)
            for (int i = 1; i<=200000; i++)
            {
                if (isPrime(i)) {
                    #pragma omp critical
                    primeCnt++;
                }
            }
            break;
    }
    double end_time = omp_get_wtime();
    printf("Total Prime Number : %d\n", primeCnt);
    printf("Execution Time : %lfms\n", (end_time - start_time) * 1000);
    return 0;
}