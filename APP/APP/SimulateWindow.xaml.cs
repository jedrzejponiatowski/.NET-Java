using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace APP
{
    /// <summary>
    /// Interaction logic for SimulateWindow.xaml
    /// </summary>
    public partial class SimulateWindow : Window
    {
        /// <summary>
        /// Set up when opened. Creates connection to DB
        /// and lists all contacts
        /// </summary>
        public SimulateWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            ContactsList2.ItemsSource = context.TakeAllPersons();
        }

        private void NameTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
        /// <summary>
        /// Closes window.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// Takes selected contact and deletes all loans connected to it,
        /// then change value of the wallet
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnSimulate(object sender, RoutedEventArgs e)
        {
            if (ContactsList2.SelectedItem == null)
            {
                return;
            }
            string pp = (string)ContactsList2.SelectedItems[0];
            string IDstring = new string(pp.TakeWhile(Char.IsDigit).ToArray());
            int ID = int.Parse(IDstring);

            var context = new PaymentsContext();

            IList<LoanGranted> lGranted;
            lGranted = context.LoansGranted.ToList();
            foreach (var loan in lGranted)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                if (temp.ID == ID)
                {
                    context.DeleteLoanGranted(loan);
                }
            }

            IList<LoanObtained> lObtained;
            lObtained = context.LoansObtained.ToList();
            foreach (var loan in lObtained)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                if (temp.ID == ID)
                {
                    context.DeleteLoanObtained(loan);
                }
            }

            this.Close();
        }
        /// <summary>
        /// Takes selected contact and sums up all loans by it
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnCheck(object sender, RoutedEventArgs e)
        {
            if (ContactsList2.SelectedItem == null)
            {
                return;
            }
            var pp = ContactsList2.SelectedItems[0] as string;
            string IDstring = new string(pp.TakeWhile(Char.IsDigit).ToArray());
            int ID = int.Parse(IDstring);

            var context = new PaymentsContext();

            string nameText = context.GetPersonWithID(ID).FirstName + " " + context.GetPersonWithID(ID).LastName;
            NameTextBox.Text = nameText;

            IList<LoanGranted> lGranted;
            lGranted = context.LoansGranted.ToList();
            int lGrantedCounter = 0;
            foreach (var loan in lGranted)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                if (temp.ID == ID)
                {
                    lGrantedCounter += loan.LoanSize;
                }
            }
            YouReceiveBox.Text = lGrantedCounter.ToString();

            IList<LoanObtained> lObtained;
            lObtained = context.LoansObtained.ToList();
            int lObtainedCounter = 0;
            foreach (var loan in lObtained)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                if (temp.ID == ID)
                {
                    lObtainedCounter += loan.LoanSize;
                }
            }
            YouPayBox.Text = lObtainedCounter.ToString();
        }
        /// <summary>
        /// Sums loans values
        /// </summary>
        /// <param name="ObtainedSum">Sum of loans in plus</param>
        /// <param name="GainedSum">Sum of loans in minus</param>
        /// <returns>Value of the </returns>
        int SumLoans(int ObtainedSum, int GainedSum)
        {
            return ObtainedSum + GainedSum;
        }
    }
}
