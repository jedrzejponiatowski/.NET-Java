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
using SendGrid;
using SendGrid.Helpers.Mail;
using System;
using System.Threading.Tasks;


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
            NewPaymentWindow win2 = new NewPaymentWindow();
            win2.ShowDialog();
            MainWindowRefresh();
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

        private void btnErasePayment(object sender, RoutedEventArgs e)
        {
            ErasePaymentWindow win2 = new ErasePaymentWindow();
            //var context = new PaymentsContext();
            //context.PrintAllLoans();
            win2.ShowDialog();
            MainWindowRefresh();
        }

        private void btnSimulate(object sender, RoutedEventArgs e)
        {
            SimulateWindow win2 = new SimulateWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }

        private void BtnNotification(object sender, RoutedEventArgs e)
        {
            NotificationWindow win2 = new NotificationWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
    }
}
