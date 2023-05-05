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
    /// Interaction logic for NewContactWindow.xaml
    /// </summary>
    public partial class NewContactWindow : Window
    {
        /// <summary>
        /// Set up window when opened
        /// </summary>
        public NewContactWindow()
        {
            InitializeComponent();
        }
        /// <summary>
        /// Takes data from form and adds it to database
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnSubmit(object sender, RoutedEventArgs e)
        {
            string firstName = TextBoxFirstName.Text.Trim();
            string lastName = TextBoxLastName.Text.Trim();
            string e_mail = TextBoxEMail.Text.Trim();
            string phone = TextBoxPhoneNumber.Text.Trim();
            Console.WriteLine(firstName);
            Console.WriteLine(lastName);
            Console.WriteLine(e_mail);
            Console.WriteLine(phone);

            Person person = new Person() { FirstName=firstName, LastName=lastName, E_Mail=e_mail, PhoneNumber=phone};
            var context = new PaymentsContext();
            context.AddNewPerson(person);
            
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
    }
}
