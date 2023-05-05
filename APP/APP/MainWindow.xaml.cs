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
        /// <summary>
        /// Statement of the main window of aplication
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
            Console.WriteLine("Init commit");

            var context = new PaymentsContext();

            context.PrintAllPersons();
            context.PrintAllLoans();


            MainWindowRefresh();
        }
        /// <summary>
        /// Reloads all data from database that is shown at main window
        /// </summary>
        public void MainWindowRefresh()
        {
            var context = new PaymentsContext();
            ContactsList.ItemsSource = context.TakeAllPersons();
            GrantedList.ItemsSource = context.TakeAllGrants();
            ObtainsList.ItemsSource = context.TakeAllObtains();
        }
        /// <summary>
        /// Definition of click action on button AddPayment.
        /// Opens form window to add contacts.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void btnAddCont(object sender, RoutedEventArgs e)
        {
            NewPaymentWindow win2 = new NewPaymentWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
        /// <summary>
        /// Definition of click action on button AddContact.
        /// Opens form window to add contacts.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnAddContact(object sender, RoutedEventArgs e)
        {
            NewContactWindow win2 = new NewContactWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
        /// <summary>
        /// Definition of click action on button EraseContact.
        /// Opens form window to erase contacts.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnEraseContact(object sender, RoutedEventArgs e)
        {
            EraseContactWindow win2 = new EraseContactWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
        /// <summary>
        /// Definition of click action on button ErasePayment.
        /// Opens form window to erase contacts.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void btnErasePayment(object sender, RoutedEventArgs e)
        {
            ErasePaymentWindow win2 = new ErasePaymentWindow();
            //var context = new PaymentsContext();
            //context.PrintAllLoans();
            win2.ShowDialog();
            MainWindowRefresh();
        }
        /// <summary>
        /// Definition of click action on button Simulate.
        /// Opens form window to simulate and pay loans by contact.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void btnSimulate(object sender, RoutedEventArgs e)
        {
            SimulateWindow win2 = new SimulateWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
        /// <summary>
        /// Definition of click action on button Notification.
        /// Opens form window to send emails to contacts.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnNotification(object sender, RoutedEventArgs e)
        {
            NotificationWindow win2 = new NotificationWindow();
            win2.ShowDialog();
            MainWindowRefresh();
        }
    }
}
