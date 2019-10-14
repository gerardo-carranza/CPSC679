#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

int main(int argc, char const *argv[])
{
	FILE *f;
	FILE *ppm;
	//char name[strlen(argv[1])+3]; //+3 is to include ppm in title
	//strcpy(name,argv[1][]);
	//strcat(name,"ppm");

	f = fopen(argv[1],"r");
	ppm = fopen(argv[2],"w");
	float x,y,z;
	int intensity,row,col;
	int i,j,scaledintensity;
	col= -1;
	int numrows = 1280;
	int numcols = 1000;
	char str[100];
	//allocate matrix
	int ** matrix;
	matrix = malloc(sizeof(int *)*numrows);
	assert(matrix);
	for(i=0; i<numrows; i++)
	{
		matrix[i] = calloc(numcols,sizeof(int));
	}
	
	fprintf(ppm,"%s\n","P3");
	fprintf(ppm,"%d %d\n",numcols,numrows); // rows=1280 cols=1000
	fprintf(ppm,"%d\n",255); // max color
	
	while(fscanf(f,"#:Profile: %d",&col) != EOF)
	{
		if(col != -1) // if it read a Profile (and therefore a #)
		{
			while(fscanf(f,"%f %f %f %d %d",&x,&y,&z,&intensity,&row)>0)// reads coordinates (if any)
			{			
				//printf("%d %d\n",intensity,row);
				matrix[1279-row][col] = intensity; //1279-row is used since ppi file reads from bottom to top
			}
		}
		else{
			fgets(str,100,f); //goes to next line
		}
	}

	for(i=0; i<numrows; i++)
	{
		for(j=0; j<numcols; j++)
		{
		//	printf("%d\n",matrix[i][j]);
			if(matrix[i][j]==0)
			{
				fprintf(ppm, "%d %d %d\n",0,255,0);
			}
			else{
				scaledintensity = (int)(255.0*(float)matrix[i][j])/9999.0;
				fprintf(ppm,"%d %d %d\n",scaledintensity,scaledintensity,scaledintensity);
			}
		}
	}

	//FREE MATRIX
	for(i=0; i<numrows; i++)
	{
		free(matrix[i]);
	}
	free(matrix);
	
	return 0;
}