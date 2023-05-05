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
    /// Interaction logic for EraseContactWindow.xaml
    /// </summary>
    public partial class EraseContactWindow : Window
    {
        /// <summary>
        /// Set up when opened. Creates connection to DB
        /// and lists all contacts
        /// </summary>
        public EraseContactWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            ContactsList2.ItemsSource = context.TakeAllPersons();
        }
        /// <summary>
        /// Takes selected contact and deletes it from table Persons
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnEraseContact(object sender, RoutedEventArgs e)
        {
            string pp = (string) ContactsList2.SelectedItems[0];
            string IDstring = new string(pp.TakeWhile(Char.IsDigit).ToArray());
            int ID = int.Parse(IDstring);

            var context = new PaymentsContext();
            context.DeletePerson( context.GetPersonWithID(ID));
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
