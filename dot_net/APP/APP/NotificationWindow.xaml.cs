using SendGrid.Helpers.Mail;
using SendGrid;
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
    /// Interaction logic for NotificationWindow.xaml
    /// </summary>
    public partial class NotificationWindow : Window
    {
        /// <summary>
        /// Set up when opened. Creates connection to DB
        /// and lists all contacts
        /// </summary>
        public NotificationWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            ContactsList2.ItemsSource = context.TakeAllPersons();
        }

        private void NameTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
        /// <summary>
        /// Handling external API to send an email to selected contact on click
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnSend(object sender, RoutedEventArgs e)
        {
            if (ContactsList2.SelectedItem == null)
            {
                return;
            }
            string pp = (string)ContactsList2.SelectedItems[0];
            string IDstring = new string(pp.TakeWhile(Char.IsDigit).ToArray());
            int ID = int.Parse(IDstring);

            var context = new PaymentsContext();
            Person temp = context.GetPersonWithID(ID);

            this.Close();
            var from = new EmailAddress("poniatowskijedrzej@gmail.com", "Poniat");
            var subject = "Calculations :)";
            var to = new EmailAddress(temp.E_Mail, temp.FirstName + " " + temp.LastName);
            var plainTextContent = "Hey! You owe me " + YouReceiveBox.Text + "PLN!";
            var htmlContent = "Hey! You owe me " + YouReceiveBox.Text + "PLN!";
            var msg = MailHelper.CreateSingleEmail(from, to, subject, plainTextContent, htmlContent);
            Execute(msg);
            this.Close();
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
        }
        /// <summary>
        /// Connection with API and wait for the response
        /// </summary>
        /// <param name="msg"></param>
        /// <returns></returns>
        static async Task Execute(SendGridMessage msg)
        {
            var apiKey = "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";
            var client = new SendGridClient(apiKey);
            var response = await client.SendEmailAsync(msg);
        }
    }
}
