using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace APP
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            Console.WriteLine("Init commit");

            var context = new PaymentsContext();

            context.PrintAllPersons();
            context.PrintAllLoans();

            /*
            var NewLoanObtained = new LoanObtained() { PersonID = 2, LoanSize = 400, DateOfIssue = "12.05.2020", DateOfExpiry = "12.06.2020" };
            var NewLoanGranted = new LoanGranted() { PersonID = 1, LoanSize = 700, DateOfIssue = "01.01.2021", DateOfExpiry = "01.02.2021" };
            context.AddNewLoanGranted(NewLoanGranted);
            context.AddNewLoanObtained(NewLoanObtained);

            context.PrintAllPersons();
            context.PrintAllLoans();
            */

            /*
            ContactsList.ItemsSource = context.TakeAllPersons();
            GrantedList.ItemsSource = context.TakeAllGrants();
            ObtainsList.ItemsSource = context.TakeAllObtains();
            */
            MainWindowRefresh();
        }

        public void MainWindowRefresh()
        {
            var context = new PaymentsContext();
            ContactsList.ItemsSource = context.TakeAllPersons();
            GrantedList.ItemsSource = context.TakeAllGrants();
            ObtainsList.ItemsSource = context.TakeAllObtains();
        }

        private void btnAddCont(object sender, RoutedEventArgs e)
        {
            CFormWindow win2 = new CFormWindow();
            win2.ShowDialog();
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void BtnAddContact(object sender, RoutedEventArgs e)
        {
            NewContactWindow win2 = new NewContactWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }

        private void BtnEraseContact(object sender, RoutedEventArgs e)
        {
            EraseContactWindow win2 = new EraseContactWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
    }
}
