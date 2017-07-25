package com.scmspain.controller;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scmspain.configuration.TestConfiguration;
import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.services.dto.TweetDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.context).build();
        newTweet("Wally", "Hi Mr. Wood!");
    }

    @Test
    public void shouldReturn200WhenInsertingAValidTweet() throws Exception {
        mockMvc.perform(newTweet("Prospect", "Breaking the law"))
            .andExpect(status().is(201));
    }

    @Test
    public void shouldReturn201StrippingDownTheUrl() throws Exception {
        mockMvc.perform(newTweet("Schibsted Spain", "We are Schibsted Spain (look at our home page http://www.schibsted.es/), we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome!"))
                .andExpect(status().is(201));
    }
    
    @Test
    public void shouldReturn201WhenInsertingAHugeUrlWithinTheStorageLimits() throws Exception {
        mockMvc.perform(newTweet("Schibsted Spain", "We are Schibsted Spain "
        		+ "(look at our home page  http://www.schibsted.mockurl.mockurl.mockurl.mockurl"
        		+ ".mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl"
        		+ ".mockurl.mockurl.mockurl.thisurlisbecomingtoohugeforthissoftwaretostoreit"
        		+ ".butitwillbecauseitiswithinthestoragelimit.mockurl.mockurl.mockurl.mockurl.mockurl"
        		+ ".mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.butitwillbecauseitiswithinthestoragelimit.es/), "
        		+ "we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome!"))
                .andExpect(status().is(201));
    }
    
    @Test
    public void shouldReturn400WhenInsertingATweetWithAUrlLongerThanTheStorageLimit() throws Exception {
        mockMvc.perform(newTweet("Schibsted Spain", "We are Schibsted Spain (look at our home page "
        		+ "http://www.schibsted.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl."
        		+ "mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl."
        		+ "thisurlisbecomingtoohugeforthissoftwaretostoreit.howmayistoresuchahugevalueifnotinaclobfield."
        		+ "mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl.mockurl."
        		+ "mockurl.mockurl.mockurl.mockurl.mockurl.thisurlisbecomingtoohugeforthissoftwaretostoreit."
        		+ "howmayistoresuchahugevalueifnotinaclobfield.es/), we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome!"))
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn200AllPublishedTweets() throws Exception {
        mockMvc.perform(newTweet("Yo", "How are you?"));

        mockMvc.perform(get("/tweet"))
                .andExpect(status().is(200))
                .andReturn();
    }
    
    @Test
    public void shouldReturn200DiscardTweet() throws Exception{
    	mockMvc.perform(newTweet("Somebody", "Some discardable tweet"));    	
    	DiscardTweetCommand command = new DiscardTweetCommand();
    	command.setTweet(this.getAllTweets().get(0).getId());
    	String json = objectMapper.writeValueAsString(command);
    	mockMvc.perform(post("/discard")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk());
    }
    
    @Test
    public void shouldReturn200AllDiscardedTweets() throws Exception {
        mockMvc.perform(newTweet("Yo", "How are you?"));

       List<TweetDto> tweets = this.getAllTweets();
        DiscardTweetCommand command = new DiscardTweetCommand();
        Long originalId = tweets.get(0).getId();
        command.setTweet(originalId);
        
        String json = objectMapper.writeValueAsString(command);
    	mockMvc.perform(post("/discard")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json));
    	
    	 mockMvc.perform(get("/discard"))
                 .andExpect(status().is(200))
                 .andReturn();
        
    }
    
    @Test
    public void shouldReturn400WhenDiscardingTweetsTwice() throws Exception{
    	DiscardTweetCommand command = new DiscardTweetCommand();
    	command.setTweet(getAllTweets().get(0).getId());
    	MockHttpServletRequestBuilder request = post("/discard")
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper.writeValueAsString(command));
    	mockMvc.perform(request).andExpect(status().isOk());
    	mockMvc.perform(request).andExpect(status().is(400));
    }
    
    @Test
    public void shouldReturn200AndRetrieveTheMatchingItem() throws Exception{
    	mockMvc.perform(newTweet("Yo", "How are you?"));
    	Long id = getAllTweets().get(0).getId();
    	MvcResult result = mockMvc.perform(get("/tweet/"+id))
					        .andExpect(status().is(200))
					        .andReturn();
    	String content = result.getResponse().getContentAsString();
  		TweetDto tweet = objectMapper.readValue(content, TweetDto.class);
  		
  		Assert.assertEquals("The retrieved object should match the requested ID", id, tweet.getId());
    	
    }
    
    @Test
    public void shouldReturn400WhenRetrievingNonExistingTweet() throws Exception{
    	mockMvc.perform(get("/tweet/"+0L))
        .andExpect(status().is(400))
        .andReturn();
    }

    /**
     * Posts a new tweet with the desired content
     * @param publisher The publisher
     * @param tweet The text of the tweet.
     * @return A request to publish a tweet.
     */
    private MockHttpServletRequestBuilder newTweet(final String publisher, final String tweet) {
        return post("/tweet")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(format("{\"publisher\": \"%s\", \"tweet\": \"%s\"}", publisher, tweet));
    }
    
    /**
     * Retrieves all tweets and parses the JSON Response as a list.
     * @return The list of tweets from the response-
     * @throws Exception
     */
    private List<TweetDto> getAllTweets() throws Exception{
    	MvcResult getResult = mockMvc.perform(get("/tweet"))
                .andReturn();

        String content = getResult.getResponse().getContentAsString();
		List<TweetDto> tweets = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TweetDto.class));
		
		return tweets;
    }

}
