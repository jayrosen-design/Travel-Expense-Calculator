//****************************************************************************** 
// Jay Rosen
// 11/21/14
// COP2660.0M1
// Module 11 : travel_j_rosen.java
//
// This program calculates and displays the total travel expenses of a business person on a trip.
//******************************************************************************

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class travel_j_rosen extends JFrame
{
   private JPanel panel;            // A panel to hold labels and text fields
   private JPanel buttonPanel;      // A panel for the buttons
   
   private JTextField numDays;      // Number of days
   private JTextField airfare;      // Airfare
   private JTextField carRental;    // Car rental
   private JTextField milesDriven;  // Miles driven
   private JTextField parkingFees;  // Parking fees
   private JTextField taxiCharges;  // Taxi charges
   private JTextField confReg;      // Conference registration
   private JTextField lodging;      // Per-night lodging
   
   private JButton calcButton;      // Calculates everything
   private JButton resetButton;     // Resets everything
   
   // Constants
   private final double DAILY_MEAL_RATE = 37.0;
   private final double MAX_PARKING_FEES = 10.0;
   private final double MAX_TAXI = 20.0;
   private final double MAX_LODGING_PER_NIGHT = 95.0;
   private final double MILEAGE_RATE = 0.27;
                        
   /**
      Constructor
   */
   
   public travel_j_rosen()
   {
      // Set the title.
      setTitle("Travel Expenses");
      
      // Specify what happens when the close button is clicked.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Build the panel that contains the labels and text fields.
      buildPanel();
      
      // Build the panel that contains the buttons.
      buildButtonPanel();
      
      // Add the panel to the content pane.
      add(panel, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);
      
      // Pack and display the window.
      pack();
      setVisible(true);
   }
   
   /**
      The buildPanel method creates a panel containing
      other components.
   */
   
   private void buildPanel()
   {
      // Create labels prompting for the required data.
      JLabel numDaysMsg = 
           new JLabel("Number of days on the trip:");
      JLabel airfareMsg = 
           new JLabel("Amount of airfare:");
      JLabel carRentalMsg = 
          new JLabel("Amount of car rental:");
      JLabel milesDrivenMsg = 
          new JLabel("Miles driven:");
      JLabel parkingFeesMsg = 
          new JLabel("Parking fees:");
      JLabel taxiChargesMsg = 
          new JLabel("Taxi fees:");
      JLabel confRegMsg = 
          new JLabel("Conference registration:");
      JLabel lodgingMsg = 
          new JLabel("Lodging charges per night:");
      
      // Create text fields for the required data.
      numDays = new JTextField(10);
      airfare = new JTextField(10);
      carRental = new JTextField(10);
      milesDriven = new JTextField(10);
      parkingFees = new JTextField(10);
      taxiCharges = new JTextField(10);
      confReg = new JTextField(10);
      lodging = new JTextField(10);
   
      // Create a panel.
      panel = new JPanel();
      
      // Add a layout manager.
      panel.setLayout(new GridLayout(10,2));
      
      // Add the labels, text fields, and button to the panel.
      panel.add(numDaysMsg);
      panel.add(numDays);
      panel.add(airfareMsg);
      panel.add(airfare);
      panel.add(carRentalMsg);
      panel.add(carRental);
      panel.add(milesDrivenMsg);
      panel.add(milesDriven);
      panel.add(parkingFeesMsg);
      panel.add(parkingFees);
      panel.add(taxiChargesMsg);
      panel.add(taxiCharges);
      panel.add(confRegMsg);
      panel.add(confReg);
      panel.add(lodgingMsg);
      panel.add(lodging);
      
      // Put an empty border around the panel.
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 1, 10));
   }

   /**
      The buildbuttonPanel method creates a panel containing
      buttons.
   */
   
   private void buildButtonPanel()
   {
      // Create a button to calculate the expenses & reimbursement.
      calcButton = new JButton("Calculate Reimbursement");
      
      // Add an action listener to the button.
      calcButton.addActionListener(new CalcButtonListener());
      
      // Create a button to reset everything.
      resetButton = new JButton("Reset");
      
      // Add an action listener to the button.
      resetButton.addActionListener(new ResetButtonListener());

      // Put the buttons in their own panel.
      buttonPanel = new JPanel();
      buttonPanel.add(resetButton);
      buttonPanel.add(calcButton);
   }
   
   /**
      CalcButtonListener is an action listener class for the
      calcbutton component.
   */
   
   private class CalcButtonListener implements ActionListener
   {
      double days;                  // Number of days
      double air;                   // Airfare
      double carRentalActual;       // Amount spent on car rental
      double miles;                 // Mileage
      double mealsRe;               // Meals, reimbursed
      double parkingActual;         // Amount spent on parking
      double parkingRe;             // Parking, reimbursed
      double taxiActual;            // Amount spent on taxis
      double taxiRe;                // Taxi, reimbursed
      double lodgingPerNightActual; // Amount spend on lodging per night
      double lodgingRe;             // Lodging, reimbursed
      double reg;                   // Conference registration

      /**
         actionPerformed method
         @param e An ActionEvent object.
      */
      
      public void actionPerformed(ActionEvent e)
      {
         double actualExpenses;     // Actual expenses incurred
         double reimbursable;       // Reimbursable expenses
         double savings;            // Amount saved, if any
         double excess;             // Excess amount spent, if any
         String msg;                // Output message
                
         // Create a DecimalFormat object to format output.
         DecimalFormat dollar = new DecimalFormat("#,##0.00");

         // Get the data entered.
         getData();
         
         // Determine the actual expenses.   
         actualExpenses = determineActualExpenses();
         
         // Determine the reimbursement amounts.
         reimbursable = determineReimbursement();
         
         msg = "Total expenses: $" + dollar.format(actualExpenses) + "\n" +
               "Allowable expenses: $" + dollar.format(reimbursable) + "\n";

         if (actualExpenses < reimbursable)
         {
            savings = reimbursable - actualExpenses;
            msg = msg + "\nAmount saved: $" + dollar.format(savings);
         }
         else if (actualExpenses > reimbursable)
         {
            excess = actualExpenses - reimbursable;
            msg = msg + "\nAmount to be paid back: $" + dollar.format(excess);
         }
               
         // Display the results.
         JOptionPane.showMessageDialog(null, msg);
      }
      
      /**
         The getData method gets the data entered by the user.
      */
      
      private void getData()
      {
         // Get the number of days.
         days = Integer.parseInt(numDays.getText());
         // Get the airfare.
         air = Double.parseDouble(airfare.getText());
         // Get the amount spent on car rental.
         carRentalActual = Double.parseDouble(carRental.getText());
         // Get the miles driven in a private auto.
         miles = Double.parseDouble(milesDriven.getText());
         // Get the amount spent on parking.
         parkingActual = Double.parseDouble(parkingFees.getText());
         // Get the amount spent on taxis.
         taxiActual = Double.parseDouble(taxiCharges.getText());
         // Get the conference registration.
         reg = Double.parseDouble(confReg.getText());
         // Get the amount spent per night on lodging.
         lodgingPerNightActual = Double.parseDouble(lodging.getText());
      }

      /**
         The determineActualExpenses method calculates the
         expenses.
         @return The amount of expenses.
      */
   
      private double determineActualExpenses()
      {
         double actual = (days * DAILY_MEAL_RATE) + parkingActual + taxiActual +
                         (miles * MILEAGE_RATE) + (lodgingPerNightActual * days) +
                         carRentalActual + air + reg;
         return actual;
      }
      
      /**
         The determineReimbursement method determines
         the allowable reimbursement.
         @return The amount of reimbursement.
      */

      private double determineReimbursement()
      {
         double reimbursable;
         
         // Calculate meal reimbursement.
         mealsRe = days * DAILY_MEAL_RATE;
         
         // Determine parking fee reimbursement.
         if (parkingActual > (MAX_PARKING_FEES * days))
            parkingRe = MAX_PARKING_FEES * days;
         else
            parkingRe = parkingActual;
         
         // Determine taxi charge reimbursement.
         if (taxiActual > (MAX_TAXI * days))
            taxiRe = MAX_TAXI * days;
         else
            taxiRe = taxiActual;

         // Determine lodging reimbursement.
         if (lodgingPerNightActual > MAX_LODGING_PER_NIGHT)
            lodgingRe= MAX_LODGING_PER_NIGHT * days;
         else
            lodgingRe = lodgingPerNightActual * days;

         reimbursable = (days * DAILY_MEAL_RATE) + parkingRe + taxiRe +
                        (miles * MILEAGE_RATE) + lodgingRe +
                        carRentalActual + air + reg;
                        
         return reimbursable;

      }
   }  // end of inner class
   
   /**
      ResetButtonListener is an action listener class for the
      resetButton component.
   */

   private class ResetButtonListener implements ActionListener
   {
      /**
         actionPerformed method
         @param e An ActionEvent object.
      */
            
      public void actionPerformed(ActionEvent e)
      {
         // Reset all the text fields to 0.
         numDays.setText("0");
         airfare.setText("0");
         carRental.setText("0");
         milesDriven.setText("0");
         parkingFees.setText("0");
         taxiCharges.setText("0");
         confReg.setText("0");
         lodging.setText("0");
      }
   } // End of inner class
   
   /**
      The main method creates an instance of the
      class, causing it to display its window.
   */

   public static void main(String[] args)
   {
      TravelExpenses te = new TravelExpenses();
   }  
}
