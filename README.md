# WebCrawler to download mails from Apache Archive

This sample demo app is meant to access crawl URL - "http://mail-archives.apache.org/mod_mbox/maven-users/"
and will download all mails pertaining to a specific year.


# Solution:

* Access/parsing HTML resources is done using JSoup library.
* Access the base URL and identify links for each month.
* Concurrently access each month URL, and get links for all sub-pages. (If count of mails is higher than threshold, then pagination is applied).
* Concurrently navigate to each sub-page URL and extract links for all mails in given page.
* Download mail content and save to file system as text file.

# Config file:

* Provision to configure Mail URL, Target Year, Connection time out value, download path.

# Running the Application


* Ensure that maven is installed and configured.
* To package the application (this will create fat jar with all dependencies) 

mvn package

* To the run app, run below command

<b>java -Dlog4j.configurationFile=file:./log4j.xml -jar target/WebCrawler-0.0.1-SNAPSHOT.jar</b>

* If executed successfully, below message will be shown:

	connecting to url :: http://mail-archives.apache.org/mod_mbox/maven-users/

	Targer Year  ::  [2015],  Mails Available for  ::  [12] months

	Mails Saved to path      :: D:\WebCrawler\mail_download\2017-07-03-05-55-19

	Total Processing time    :: 35.22 seconds

	Total mails downloaded   :: 1858
	
# Note:

* Count of downloaded mails may vary depending on network/connection time out. Please check ./log/webcrawler.log for connection timeout info.
