-module(data_hidden_make).
-include("../include/comm.hrl").

-export([get/1,get_ids/0]).

% 
get(1001)->
	#d_hidden_make{
		items_id     = 1001,            %% 打造后物品ID
		make         = [{36001,1,100,0},{34057,20,10,20210},{34049,20,10,20190},{34053,20,10,20200}],%% 打造列表
		level        = 101             %% 层数
	};
get(1002)->
	#d_hidden_make{
		items_id     = 1002,            %% 打造后物品ID
		make         = [{36005,1,100,0},{34053,20,10,20200},{34045,20,10,20180},{34069,20,10,20240}],%% 打造列表
		level        = 101             %% 层数
	};
get(1003)->
	#d_hidden_make{
		items_id     = 1003,            %% 打造后物品ID
		make         = [{36009,1,100,0},{34065,20,10,20230},{34061,20,10,20220},{34073,20,10,20250}],%% 打造列表
		level        = 101             %% 层数
	};
get(1004)->
	#d_hidden_make{
		items_id     = 1004,            %% 打造后物品ID
		make         = [{36013,1,100,0},{34065,20,10,20230},{34049,20,10,20190},{34073,20,10,20250}],%% 打造列表
		level        = 101             %% 层数
	};
get(1005)->
	#d_hidden_make{
		items_id     = 1005,            %% 打造后物品ID
		make         = [{36017,1,100,0},{34065,20,10,20230},{34057,20,10,20210},{34061,20,10,20220}],%% 打造列表
		level        = 101             %% 层数
	};
get(1006)->
	#d_hidden_make{
		items_id     = 1006,            %% 打造后物品ID
		make         = [{36021,1,100,0},{34045,20,10,20180},{34049,20,10,20190},{34073,20,10,20250}],%% 打造列表
		level        = 101             %% 层数
	};
get(1007)->
	#d_hidden_make{
		items_id     = 1007,            %% 打造后物品ID
		make         = [{36025,1,100,0},{34069,20,10,20240},{34057,20,10,20210},{34073,20,10,20250}],%% 打造列表
		level        = 101             %% 层数
	};
get(1008)->
	#d_hidden_make{
		items_id     = 1008,            %% 打造后物品ID
		make         = [{36029,1,100,0},{34065,20,10,20230},{34069,20,10,20240},{34101,20,10,20320}],%% 打造列表
		level        = 101             %% 层数
	};
get(2001)->
	#d_hidden_make{
		items_id     = 2001,            %% 打造后物品ID
		make         = [{36033,1,100,0},{34093,50,10,20300},{34085,50,10,20280},{34101,50,10,20320}],%% 打造列表
		level        = 201             %% 层数
	};
get(2002)->
	#d_hidden_make{
		items_id     = 2002,            %% 打造后物品ID
		make         = [{36037,1,100,0},{34105,50,10,20330},{34081,50,10,20270},{34077,50,10,20260}],%% 打造列表
		level        = 201             %% 层数
	};
get(2003)->
	#d_hidden_make{
		items_id     = 2003,            %% 打造后物品ID
		make         = [{36041,1,100,0},{34093,50,10,20300},{34085,50,10,20280},{34105,50,10,20330}],%% 打造列表
		level        = 201             %% 层数
	};
get(2004)->
	#d_hidden_make{
		items_id     = 2004,            %% 打造后物品ID
		make         = [{36045,1,100,0},{34077,50,10,20260},{34081,50,10,20270},{34101,50,10,20320}],%% 打造列表
		level        = 201             %% 层数
	};
get(2005)->
	#d_hidden_make{
		items_id     = 2005,            %% 打造后物品ID
		make         = [{36049,1,100,0},{34101,50,10,20320},{34093,50,10,20300},{34085,50,10,20280}],%% 打造列表
		level        = 201             %% 层数
	};
get(2006)->
	#d_hidden_make{
		items_id     = 2006,            %% 打造后物品ID
		make         = [{36053,1,100,0},{34085,50,10,20280},{34097,50,10,20310},{34105,50,10,20330}],%% 打造列表
		level        = 201             %% 层数
	};
get(2007)->
	#d_hidden_make{
		items_id     = 2007,            %% 打造后物品ID
		make         = [{36057,1,100,0},{34077,50,10,20260},{34093,50,10,20300},{34097,50,10,20310}],%% 打造列表
		level        = 201             %% 层数
	};
get(2008)->
	#d_hidden_make{
		items_id     = 2008,            %% 打造后物品ID
		make         = [{36061,1,100,0},{34105,50,10,20330},{34085,50,10,20280},{34101,50,10,20320}],%% 打造列表
		level        = 201             %% 层数
	};
get(3001)->
	#d_hidden_make{
		items_id     = 3001,            %% 打造后物品ID
		make         = [{36065,1,100,0},{34129,100,10,20390},{34125,100,10,20380},{34133,100,10,20400}],%% 打造列表
		level        = 301             %% 层数
	};
get(3002)->
	#d_hidden_make{
		items_id     = 3002,            %% 打造后物品ID
		make         = [{36069,1,100,0},{34125,100,10,20380},{34113,100,10,20350},{34137,100,10,20410}],%% 打造列表
		level        = 301             %% 层数
	};
get(3003)->
	#d_hidden_make{
		items_id     = 3003,            %% 打造后物品ID
		make         = [{36073,1,100,0},{34137,100,10,20410},{34117,100,10,20360},{34129,100,10,20390}],%% 打造列表
		level        = 301             %% 层数
	};
get(3004)->
	#d_hidden_make{
		items_id     = 3004,            %% 打造后物品ID
		make         = [{36077,1,100,0},{34129,100,10,20390},{34113,100,10,20350},{34133,100,10,20400}],%% 打造列表
		level        = 301             %% 层数
	};
get(3005)->
	#d_hidden_make{
		items_id     = 3005,            %% 打造后物品ID
		make         = [{36081,1,100,0},{34129,100,10,20390},{34121,100,10,20370},{34125,100,10,20380}],%% 打造列表
		level        = 301             %% 层数
	};
get(3006)->
	#d_hidden_make{
		items_id     = 3006,            %% 打造后物品ID
		make         = [{36085,1,100,0},{34133,100,10,20400},{34125,100,10,20380},{34129,100,10,20390}],%% 打造列表
		level        = 301             %% 层数
	};
get(3007)->
	#d_hidden_make{
		items_id     = 3007,            %% 打造后物品ID
		make         = [{36089,1,100,0},{34137,100,10,20410},{34109,100,10,20340},{34125,100,10,20380}],%% 打造列表
		level        = 301             %% 层数
	};
get(3008)->
	#d_hidden_make{
		items_id     = 3008,            %% 打造后物品ID
		make         = [{36093,1,100,0},{34121,100,10,20370},{34117,100,10,20360},{34125,100,10,20380}],%% 打造列表
		level        = 301             %% 层数
	};
get(4001)->
	#d_hidden_make{
		items_id     = 4001,            %% 打造后物品ID
		make         = [{36097,1,100,0},{34149,180,10,20440},{34153,180,10,20450},{34141,180,10,20420}],%% 打造列表
		level        = 401             %% 层数
	};
get(4002)->
	#d_hidden_make{
		items_id     = 4002,            %% 打造后物品ID
		make         = [{36101,1,100,0},{34149,180,10,20440},{34145,180,10,20430},{34141,180,10,20420}],%% 打造列表
		level        = 401             %% 层数
	};
get(4003)->
	#d_hidden_make{
		items_id     = 4003,            %% 打造后物品ID
		make         = [{36105,1,100,0},{34149,180,10,20440},{34145,180,10,20430},{34153,180,10,20450}],%% 打造列表
		level        = 401             %% 层数
	};
get(4004)->
	#d_hidden_make{
		items_id     = 4004,            %% 打造后物品ID
		make         = [{36109,1,100,0},{34145,180,10,20430},{34149,180,10,20440},{34153,180,10,20450}],%% 打造列表
		level        = 401             %% 层数
	};
get(4005)->
	#d_hidden_make{
		items_id     = 4005,            %% 打造后物品ID
		make         = [{36113,1,100,0},{34157,180,10,20460},{34153,180,10,20450},{34141,180,10,20420}],%% 打造列表
		level        = 401             %% 层数
	};
get(4006)->
	#d_hidden_make{
		items_id     = 4006,            %% 打造后物品ID
		make         = [{36117,1,100,0},{34157,180,10,20460},{34145,180,10,20430},{34169,180,10,20490}],%% 打造列表
		level        = 401             %% 层数
	};
get(4007)->
	#d_hidden_make{
		items_id     = 4007,            %% 打造后物品ID
		make         = [{36121,1,100,0},{34161,180,10,20470},{34145,180,10,20430},{34141,180,10,20420}],%% 打造列表
		level        = 401             %% 层数
	};
get(4008)->
	#d_hidden_make{
		items_id     = 4008,            %% 打造后物品ID
		make         = [{36125,1,100,0},{34149,180,10,20440},{34153,180,10,20450},{34157,180,10,20460}],%% 打造列表
		level        = 401             %% 层数
	};
get(5001)->
	#d_hidden_make{
		items_id     = 5001,            %% 打造后物品ID
		make         = [{36097,1,100,0},{34149,300,10,20440},{34153,300,10,20450},{34141,300,10,20420}],%% 打造列表
		level        = 501             %% 层数
	};
get(5002)->
	#d_hidden_make{
		items_id     = 5002,            %% 打造后物品ID
		make         = [{36101,1,100,0},{34149,300,10,20440},{34145,300,10,20430},{34141,300,10,20420}],%% 打造列表
		level        = 501             %% 层数
	};
get(5003)->
	#d_hidden_make{
		items_id     = 5003,            %% 打造后物品ID
		make         = [{36105,1,100,0},{34149,300,10,20440},{34145,300,10,20430},{34153,300,10,20450}],%% 打造列表
		level        = 501             %% 层数
	};
get(5004)->
	#d_hidden_make{
		items_id     = 5004,            %% 打造后物品ID
		make         = [{36109,1,100,0},{34145,300,10,20430},{34149,300,10,20440},{34153,300,10,20450}],%% 打造列表
		level        = 501             %% 层数
	};
get(5005)->
	#d_hidden_make{
		items_id     = 5005,            %% 打造后物品ID
		make         = [{36113,1,100,0},{34157,300,10,20460},{34153,300,10,20450},{34141,300,10,20420}],%% 打造列表
		level        = 501             %% 层数
	};
get(5006)->
	#d_hidden_make{
		items_id     = 5006,            %% 打造后物品ID
		make         = [{36117,1,100,0},{34157,300,10,20460},{34145,300,10,20430},{34169,300,10,20490}],%% 打造列表
		level        = 501             %% 层数
	};
get(5007)->
	#d_hidden_make{
		items_id     = 5007,            %% 打造后物品ID
		make         = [{36121,1,100,0},{34161,300,10,20470},{34145,300,10,20430},{34141,300,10,20420}],%% 打造列表
		level        = 501             %% 层数
	};
get(5008)->
	#d_hidden_make{
		items_id     = 5008,            %% 打造后物品ID
		make         = [{36125,1,100,0},{34149,300,10,20440},{34153,300,10,20450},{34157,300,10,20460}],%% 打造列表
		level        = 501             %% 层数
	};
get(_)-> ?null.


%% 集合;
get_ids()->[1001,1002,1003,1004,1005,1006,1007,1008,2001,2002,2003,2004,2005,2006,2007,2008,3001,3002,3003,3004,3005,3006,3007,3008,4001,4002,4003,4004,4005,4006,4007,4008,5001,5002,5003,5004,5005,5006,5007,5008].

