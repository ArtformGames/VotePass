package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.List;

public class QuickReviewGUI {

    private QuickReviewGUI() {
    }

    public static boolean open(Player player, RequestInformation request) {
        BookUtil.BookBuilder builder = BookUtil.writtenBook();
        builder.title("#" + request.getID());
        builder.author(request.getUsername());

        List<BaseComponent[]> pages = new ArrayList<>();
        pages.add(buildOverview(player, request));

        request.getContents().forEach((index, answer) -> pages.addAll(buildAnswers(player, index, answer)));

        pages.addAll(buildComments(player, request));
        pages.add(CONFIG.RETURN.parseToLine(player, request.getID()));

        if (pages.size() > 50) return false;

        builder.pages(pages);

        BookUtil.openPlayer(player, builder.build());
        return true;
    }

    private static BaseComponent[] buildOverview(Player player, RequestInformation request) {
        RequestIconInfo iconInfo = RequestIconInfo.of(request);
        return CONFIG.OVERVIEW.parseToLine(player,
                iconInfo.displayName(), iconInfo.uuid(), iconInfo.id(), iconInfo.words(),
                iconInfo.createTime(), iconInfo.expireTime(),
                iconInfo.pros(), iconInfo.prosPercent(),
                iconInfo.cons(), iconInfo.consPercent(),
                iconInfo.abs(), iconInfo.absPercent(),
                iconInfo.passRequired(), iconInfo.passRemain(),
                iconInfo.size(), iconInfo.total()
        );
    }

    private static List<BaseComponent[]> buildAnswers(Player player, int id, RequestAnswer content) {
        List<BaseComponent[]> answers = new ArrayList<>();
        int words = content.countWords();
        if (words <= 0) {
            answers.add(CONFIG.ANSWER_EMPTY.parseToLine(player, id, content.question()));
        } else {
            answers.add(CONFIG.ANSWER_CONTENT.parseToLine(player, id, content.question(), words));
            content.answers().stream().map(answer -> BookUtil.PageBuilder.of(answer).build()).forEach(answers::add);
        }
        return answers;
    }

    private static List<BaseComponent[]> buildComments(Player player, RequestInformation request) {
        List<BaseComponent[]> commentPages = new ArrayList<>();

        int commented = request.countCommentedVotes();
        if (commented > 0) {

            commentPages.add(CONFIG.HAS_COMMENTS.parseToLine(player, request.getID(), commented));

            int i = 1;
            for (VoteInformation vote : request.getVotes()) {
                if (vote.decision() == VoteDecision.ABSTAIN || vote.comment() == null || vote.comment().isEmpty()) {
                    continue;
                }
                if (vote.isApproved()) {
                    commentPages.add(CONFIG.COMMENT_APPROVED.parseToLine(player, i, vote.voter().getDisplayName(), vote.comment()));
                } else {
                    commentPages.add(CONFIG.COMMENT_REJECTED.parseToLine(player, i, vote.voter().getDisplayName(), vote.comment()));
                }
                i++;
            }

        } else {
            commentPages.add(CONFIG.NONE_COMMENT.parseToLine(player, request.getID()));
        }

        return commentPages;
    }

    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> OVERVIEW = TextMessages.list()
                .defaults(
                        "Handle request #%(request_id)",
                        " ",
                        "&7Applicant username",
                        "&6%(name)",
                        " ",
                        "&a&lApproved&7: %(pros_amount)&8(%(pros_ratio)%)",
                        "&c&lRejected&7: %(cons_amount)&8(%(cons_ratio)%)",
                        "&e&lAbstain&7: %(abstains_amount)&8(%(abstains_ratio)%)",
                        " ",
                        "&fThis request requires &e%(pass_remain)&7/%(pass_required) &fmore approves to pass.",
                        " ",
                        "&8Please turn to the next page for detailed answers."
                ).params(
                        "name", "uuid",
                        "request_id", "request_words",
                        "create_time", "close_time",
                        "pros_amount", "pros_ratio",
                        "cons_amount", "cons_ratio",
                        "abstains_amount", "abstains_ratio",
                        "pass_required", "pass_remain",
                        "votes_amount", "total_amount"
                ).build();


        public static final ConfiguredMessageList<BaseComponent[]> ANSWER_EMPTY = TextMessages.list()
                .defaults(
                        "&8#%(index): &0%(question)",
                        " ",
                        "&7&oThis user did not answer the question.",
                        "&8Please keep turning the page to see other answers."
                ).params("index", "question").build();

        public static final ConfiguredMessageList<BaseComponent[]> ANSWER_CONTENT = TextMessages.list()
                .defaults(
                        "&8#%(index): &0%(question)",
                        " ",
                        "&8This user answered %(words) words,",
                        "&8Please keep turning the page to see his answers."
                ).params("index", "question", "words").build();

        public static final ConfiguredMessageList<BaseComponent[]> HAS_COMMENTS = TextMessages.list()
                .defaults(
                        "&8This request has a size of %(amount) comments,",
                        " ",
                        "&7&oIf you want to express your personal opinion, please return to process this request in the next page.",
                        "&8Please keep turning the page to see others opinions."
                ).params("id", "amount").build();

        public static final ConfiguredMessageList<BaseComponent[]> NONE_COMMENT = TextMessages.list()
                .defaults(
                        "&8There are currently no other votes on this request.",
                        " ",
                        "&7&oIf you want to express your personal opinion, please return to process this request in the next page."
                ).params("id").build();

        public static final ConfiguredMessageList<BaseComponent[]> COMMENT_APPROVED = TextMessages.list()
                .defaults(
                        "&8#%(index) &7from &9%(voter)",
                        " ",
                        "&7This vote is &a&lApproved &7.",
                        "&8",
                        "&8%(comment)"
                ).params("index", "voter", "comment").build();

        public static final ConfiguredMessageList<BaseComponent[]> COMMENT_REJECTED = TextMessages.list()
                .defaults(
                        "&8#%(index) &7from &9%(voter)",
                        " ",
                        "&7This vote is &C&lRejected&7.",
                        "&8",
                        "&8%(comment)"
                ).params("index", "voter", "comment").build();

        public static final ConfiguredMessageList<BaseComponent[]> RETURN = TextMessages.list()
                .defaults(
                        "All answers have been displayed, please click the text below to go to the detailed interface for processing.",
                        " ",
                        "[&a&l[Handle request]](hover=Click to return to the details page and continue processing related answers. run_command=/votepass handle %(id))"
                ).params("id")
                .build();


    }

}
