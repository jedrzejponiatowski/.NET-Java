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
    /// Logika interakcji dla klasy ErasePaymentWindow.xaml
    /// </summary>
    public partial class ErasePaymentWindow : Window
    {
        public ErasePaymentWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();

            IList<LoanGranted> lGranted;
            IList<string> GrantedOutput = new List<string>();
            lGranted = context.LoansGranted.ToList();
            foreach (var loan in lGranted)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                GrantedOutput.Add($"{loan.LoanGrantedID}.{temp.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
            }
            GrantedList_EPW.ItemsSource = GrantedOutput;
            

            IList<LoanObtained> lObtained;
            IList<string> ObtainedOutput = new List<string>();
            lObtained = context.LoansObtained.ToList();
            foreach (var loan in lObtained)
            {
                Person temp = context.GetPersonWithID(loan.PersonID);
                ObtainedOutput.Add($"{loan.LoanObtainedID}.{temp.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
            }
            ObtainedList_EPW.ItemsSource = ObtainedOutput;
        }

        private void BtnErase(object sender, RoutedEventArgs e)
        {
            var context = new PaymentsContext();

            foreach (var lg in GrantedList_EPW.SelectedItems)
            {
                Console.WriteLine(lg.ToString());
                string IDstring = new string(lg.ToString().TakeWhile(Char.IsDigit).ToArray());
                context.DeleteLoanGranted(context.GetLoanGrantedwithID(int.Parse(IDstring)));
            }

            foreach (var lg in ObtainedList_EPW.SelectedItems)
            {
                Console.WriteLine(lg.ToString());
                string IDstring = new string(lg.ToString().TakeWhile(Char.IsDigit).ToArray());
                context.DeleteLoanObtained(context.GetLoanObtainedwithID(int.Parse(IDstring)));
            }

            this.Close();
        }

        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }


    }


}
