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
    /// Logika interakcji dla klasy SimulateWindow.xaml
    /// </summary>
    public partial class SimulateWindow : Window
    {
        public SimulateWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            ContactsList2.ItemsSource = context.TakeAllPersons();
        }

        private void NameTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

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
    }
}
