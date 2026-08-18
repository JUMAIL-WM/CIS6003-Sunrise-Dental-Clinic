/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

/**
 *
 * @author Future_Mind
 */
public class BillingCalculator 
{
public static double calculateTotalFee(String treatmentType, double consultationFee) 
    {
        double treatmentFee = 0.0;
        
        if (treatmentType == null) return consultationFee;

        switch (treatmentType) 
        {
            case "Cleaning":
                treatmentFee = 3000.00;
                break;
            case "Filling":
                treatmentFee = 4500.00;
                break;
            case "Root Canal":
                treatmentFee = 15000.00;
                break;
            case "Extraction":
                treatmentFee = 4000.00;
                break;
            case "Teeth Whitening":
                treatmentFee = 10000.00;
                break;
            default:
                treatmentFee = 2000.00;
                break;
        }
        return treatmentFee + consultationFee;
    }    
}
