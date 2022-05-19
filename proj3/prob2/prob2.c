#include <omp.h>
#include <stdio.h>

long num_steps = 10000000; 
double step;

void main (int argc, char *argv[])
{ 
	long i; double x, pi, sum = 0.0;
	double start_time, end_time;

    int scheduleType = atoi(argv[1]);
    int chunkSize = atoi(argv[2]);
    int threadNum = atoi(argv[3]);

	start_time = omp_get_wtime();
    omp_set_num_threads(threadNum);
	step = 1.0/(double) num_steps;
    switch (scheduleType)
    {
        case 1:
            // static
            #pragma omp parallel for schedule(static, chunkSize) reduction(+:sum) private(x)
            for (i=0;i< num_steps; i++) {
                x = (i+0.5)*step;
                sum = sum + 4.0/(1.0+x*x);
            }
            break;
        case 2:
            // dynamic
            #pragma omp parallel for schedule(dynamic, chunkSize) reduction(+:sum) private(x)
            for (i=0;i< num_steps; i++){
                x = (i+0.5)*step;
                sum = sum + 4.0/(1.0+x*x);
            }
            break;
        case 3:
            // guided
            #pragma omp parallel for schedule(guided, chunkSize) reduction(+:sum) private(x)
            for (i=0;i< num_steps; i++){
                x = (i+0.5)*step;
                sum = sum + 4.0/(1.0+x*x);
            }
            break;
    }
    
	pi = step * sum;
	end_time = omp_get_wtime();
	double timeDiff = end_time - start_time;
        printf("Execution Time : %lfms\n", timeDiff);

	printf("pi=%.24lf\n",pi);
}