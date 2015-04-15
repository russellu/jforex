package nifti;

import java.util.ArrayList;

import niftijio.NiftiVolume;

public class Array2Nifti {
	
	public static void writeArrayList4d(ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> input,String name)throws Exception{		
		int nx = input.size() ;
        int ny = input.get(0).size() ;
        int nz = input.get(0).get(0).size() ;
        int nt = input.get(0).get(0).get(0).size() ;
        NiftiVolume volume = new NiftiVolume(nx, ny, nz, nt);
        for(int i=0;i<nx;i++)
        	for(int j=0;j<ny;j++)
        		for(int k=0;k<nz;k++)
        			for(int l=0;l<nt;l++)
                        volume.data[i][j][k][l] = input.get(i).get(j).get(k).get(l) ;
               
        volume.write(name+".nii.gz") ;						
	}
	
	public static void main(String[]args)throws Exception{
		 int nx = 50;
         int ny = 50;
         int nz = 50;
         int dim = 50;   
         NiftiVolume volume = new NiftiVolume(nx, ny, nz, dim);
         for (int d = 0; d < dim; d++)
             for (int k = 0; k < nz; k++)
                 for (int j = 0; j < ny; j++)
                     for (int i = 0; i < nx; i++)
                         volume.data[i][j][k][d] = Math.random()/1000 ;
                
         volume.write("example.nii.gz") ;		
	}
}
