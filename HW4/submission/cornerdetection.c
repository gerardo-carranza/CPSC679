#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char const *argv[])
{
	int numcols, numrows, i,j,m,n,value;
	double max =0; 
	char str[100];
	int R,G,B;

	FILE *ppm; 
	FILE *new_image; 
	ppm = fopen(argv[1],"r"); //read in ppm image

	fgets(str,100,ppm); //skips P#
	fgets(str,100,ppm); // #...
	fscanf(ppm,"%d %d\n",&numcols,&numrows); 

	double ** matrix;
	double ** Iy;
	double ** Ix; 
	double ** Ixy;
	double ** Ixx; 
	double ** Iyy;
	double ** R_matrix;
	matrix = malloc(sizeof(double *)*numrows);
	Ix = malloc(sizeof(double *)*numrows);
	Iy = malloc(sizeof(double *)*numrows);
	Ixx = malloc(sizeof(double *)*numrows);
	Iyy = malloc(sizeof(double *)*numrows);
	Ixy = malloc(sizeof(double *)*numrows);
	R_matrix = malloc(sizeof(double *)*numrows);

	for(i=0; i<numrows; i++)
	{
		matrix[i] = calloc(numcols,sizeof(double));
		Ix[i] = calloc(numcols,sizeof(double));
		Iy[i] = calloc(numcols,sizeof(double));
		Ixx[i] = calloc(numcols,sizeof(double));
		Iyy[i] = calloc(numcols,sizeof(double));
		Ixy[i] = calloc(numcols,sizeof(double));
		R_matrix[i] = calloc(numcols,sizeof(double));
			
	}

	while(fscanf(ppm,"%d %d %d",&R,&G,&B) != EOF)
	{
		for(i=0; i<numrows; i++){
			for(j=0; j<numcols; j++)
			{
				fscanf(ppm,"%d %d %d",&R,&G,&B);
				matrix[i][j] = 0.3*R+0.5*G+ 0.5*B; //Dpes this end up being a double?		
				//printf(	"%f\n",matrix[i][j]);		
			}
		}
		
	}
	//printf("I'm about to compute Ix and Iy\n");
	//computing the mask Ix and Iy
	for(i = 2; i<numrows-2; i++)
	{
		for(j=2; j<numcols-2; j++)
		{
			Ix[i][j] = -2*matrix[i-2][j] + -1*matrix[i-1][j] + matrix[i+1][j] + 2*matrix[i+2][j];
			Iy[i][j] = -2*matrix[i][j-2] + -1*matrix[i][j-1] + matrix[i][j+1] + 2*matrix[i][j+2];
		}
	}


	//printf("I'm about to compute Ixx,Iyy,Ixy\n");
	
	//computing Ixx, Iyy, Ixy

	for(i=9; i<numrows-9; i++)
	{
		for(j=9; j<numcols-9; j++)
		{
			for(m=-7; m<=7; m++)
			{
				for(n=-7; n<=7; n++)
				//printf("i = %d and j = %d\n",i,j);
				Ixx[i][j]+= Ix[i+m][j+n]*Ix[i+m][j+n];
				Iyy[i][j]+= Iy[i+m][j+n]*Iy[i+m][j+n];
				Ixy[i][j]+= Ix[i+m][j+n]*Iy[i+m][j+n];
			}
			Ixx[i][j]=Ixx[i][j]/225;
			Iyy[i][j]=Ixx[i][j]/225;
			Ixy[i][j]=Ixx[i][j]/225;
		}
	}
	//printf("I'm going to compute R now\n");

	for(i=9; i<numrows-9; i++)
	{
		for(j=9; j<numcols-9; j++)
		{
			R_matrix[i][j] = Ixx[i][j]*Iyy[i][j] - Ixy[i][j]*Ixy[i][j] - .04*(Ixx[i][j]+Iyy[i][j]);
			if(R_matrix[i][j] > max)
			{
				max = R_matrix[i][j]; 
			}
			//printf("R[i][j] = %f \n",R_matrix[i][j]);
		}
	}


	//output values to a ppm! Wahoo!
	//printf("Let's go!\n");
	new_image = fopen(argv[2],"w");
	fprintf(new_image,"P3\n");
	fprintf(new_image, "%d %d \n",numcols,numrows);
	fprintf(new_image, "%d\n",255);
	for(i=0; i<numrows; i++)
	{
		for(j=0; j<numcols; j++)
		{
			value = (int)((R_matrix[i][j]*255)/max);
			fprintf(new_image,"%d %d %d\n",value,value,value);
		}
	}

	fclose(ppm);
	fclose(new_image);




	//FREE MATRIX
	for(i=0; i<numrows; i++)
	{
		free(matrix[i]);
	}
	free(matrix);
	




	return 0;
}